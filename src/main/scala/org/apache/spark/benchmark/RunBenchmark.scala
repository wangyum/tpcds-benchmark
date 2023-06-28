package org.apache.spark.benchmark

/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.{ByteArrayOutputStream, InputStream}
import java.nio.charset.StandardCharsets.UTF_8
import java.sql.{Connection, DriverManager, ResultSet}

object RunBenchmark {

  def main(args: Array[String]): Unit = {
    if (args.length < 3) {
      throw new IllegalArgumentException(
        "Please specify the JDBC url, tpcds/tpcds-modifiedQueries/tpcds-v2.7.0 and SQL ids.")
    }
    val url = args(0)
    val category = args(1)
    val sqlIds = args(2).split(",")
    val pattern = if (args.length == 4) Option(args(3)) else None

    val base = getConn(url)
    val baseStatement = base.createStatement()
    val feature = getConn(url)
    val featureStatement = feature.createStatement()

    baseStatement.executeQuery("set spark.sql.aggregate.adaptivePartialAggregationThreshold=0")
    baseStatement.executeQuery("set spark.sql.optimizer.partialAggregationOptimization.enabled=false")
    baseStatement.executeQuery("set spark.sql.ui.explainMode=COST")

    featureStatement.executeQuery("set spark.sql.aggregate.adaptivePartialAggregationThreshold=60000")
    featureStatement.executeQuery("set spark.sql.optimizer.partialAggregationOptimization.enabled=true")
    featureStatement.executeQuery("set spark.sql.ui.explainMode=COST")
    featureStatement.executeQuery("set spark.sql.cbo.enabled=false")
    featureStatement.executeQuery("set spark.sql.cbo.joinReorder.enabled=true")

    sqlIds.foreach { sqlID =>
      Option(getClass.getClassLoader.getResourceAsStream(s"$category/$sqlID.sql")) match {
        case Some(is) =>
          try {
            val content = fileToString(is)
            val sqlStr = s"--${category}_${sqlID} \n ${content}"

            val shouldRun = pattern.isEmpty ||
              getResult(featureStatement.executeQuery("explain cost " + content))
              .exists(_.contains(pattern.get))
            if (shouldRun) {
              var baseResult: Seq[String] = Seq.empty
              var featureResult: Seq[String] = Seq.empty

              val benchmark = new Benchmark(s"Benchmark ${sqlID}", 0, minNumIters = 3)
              benchmark.addCase("Feature disabled") { _ =>
                baseResult = getResult(baseStatement.executeQuery(sqlStr))
              }

              benchmark.addCase("Feature enabled") { _ =>
                featureResult = getResult(featureStatement.executeQuery(sqlStr))
              }
              benchmark.run()

              val isResultMatched = baseResult.sorted == featureResult.sorted
              println(s"$sqlID has finished and the result match: ${isResultMatched}.")
            } else {
              println(s"Skip to run $sqlID.")
            }
          } catch {
            case e: Exception =>
              println(s"Failed to run $sqlID. ${e.getMessage}.")
          }
        case _ =>
          println(s"It seems ${category} does not contains $sqlID.")
      }
    }
  }

  private def getConn(url: String): Connection = {
    Class.forName("org.apache.hive.jdbc.HiveDriver")
    val conn = DriverManager.getConnection(url, "benchmark", "mypassword")
    val stat = conn.createStatement()
    // stat.executeQuery("set role admin")
    stat.executeQuery("use hermes_tpcds5t")
    stat.close()
    conn
  }

  private def fileToString(inStream: InputStream): String = {
    val outStream = new ByteArrayOutputStream
    try {
      var reading = true
      while (reading) {
        inStream.read() match {
          case -1 => reading = false
          case c => outStream.write(c)
        }
      }
      outStream.flush()
    } finally {
      inStream.close()
    }
    new String(outStream.toByteArray, UTF_8)
  }

  private def getResult(rs: ResultSet): Seq[String] = {
    val cols = rs.getMetaData.getColumnCount
    val buildStr = () => (for (i <- 1 to cols) yield {
      rs.getObject(i)
    }).mkString("\t")

    val ret = Iterator.continually(rs.next()).takeWhile(identity).map(_ => buildStr()).toSeq.sorted
    rs.close()
    ret
  }
}
