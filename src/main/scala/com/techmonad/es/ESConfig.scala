package com.techmonad.es

import com.techmonad.util.Configuration
import org.apache.http.HttpHost
import org.elasticsearch.client.{RestClient, RestHighLevelClient}

import scala.collection.JavaConverters._

trait ESConfig {

  lazy val esConfig = Configuration.config.getConfig("es")

  lazy val indexName: String = esConfig.getString("catalogue.index")

  lazy val restClient: RestHighLevelClient = {
    val nodes = esConfig.getStringList("nodes")
    val port = esConfig.getInt("port")
    val hosts = nodes.asScala.map { host => new HttpHost(host, port, "http") }
    new RestHighLevelClient(RestClient.builder(hosts: _*))
  }


}

