package com.techmonad.es

import java.net.InetAddress

import com.techmonad.util.Configuration
import org.elasticsearch.client.Client
import org.elasticsearch.common.settings.Settings
import org.elasticsearch.common.transport.TransportAddress
import org.elasticsearch.transport.client.PreBuiltTransportClient

import scala.collection.JavaConverters._

trait ESConfig {

  lazy val esConfig = Configuration.config.getConfig("es")

  lazy val indexName: String = esConfig.getString("catalogue.index")

  lazy val client: Client = {
    val nodes = esConfig.getStringList("nodes")
    val port = esConfig.getInt("port")
    val hosts = nodes.asScala.map { host => new TransportAddress(InetAddress.getByName(host), port) }
    val settings: Settings = Settings.EMPTY // or .builder().put("cluster.name", "myClusterName").build();
    new PreBuiltTransportClient(settings).addTransportAddresses(hosts: _*)
  }


}

