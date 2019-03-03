package com.techmonad.es

import com.techmonad.json.JsonHelper
import com.techmonad.logger.Logging
import org.elasticsearch.action.bulk.BulkRequest
import org.elasticsearch.action.index.IndexRequest
import org.elasticsearch.action.search.SearchRequest
import org.elasticsearch.client.{RequestOptions, RestHighLevelClient}
import org.elasticsearch.common.xcontent.XContentType
import org.elasticsearch.index.query.BoolQueryBuilder
import org.elasticsearch.index.query.QueryBuilders._
import org.elasticsearch.search.builder.SearchSourceBuilder


trait CatalogueRepository extends JsonHelper with Logging {

  protected val restClient: RestHighLevelClient

  protected val indexName: String

  def ingest(doc: Map[String, Any]): Boolean = ingestAll(List(doc))

  def ingestAll(docs: List[Map[String, Any]]): Boolean = {
    info("ingesting docs.... " + docs.length)
    val bulkRequest = getBulkRequestBuilder(docs)
    val response = restClient.bulk(bulkRequest, RequestOptions.DEFAULT)
    response.hasFailures
  }

  private def getBulkRequestBuilder(docs: List[Map[String, Any]]): BulkRequest = {
    val bulkRequest = new BulkRequest()
    docs foreach {
      doc =>
        val docId = doc("id").toString
        val `type` = doc("type").toString
        bulkRequest.add(
          new IndexRequest(indexName, `type`, docId)
            .source(write(doc - `type`), XContentType.JSON)
        )
    }
    bulkRequest
  }

  def searchByQuery(params: Map[String, Any]): String = {
    val query = getQuery(params)
    info("Search query: " + query.toString)
    val response =
      restClient.search(new SearchRequest(indexName).source(new SearchSourceBuilder().query(query)), RequestOptions.DEFAULT)
    write(response.getHits.getHits.map(doc => parse(doc.getSourceAsString())))
  }

  private def getQuery(params: Map[String, Any]): BoolQueryBuilder = {
    val queryBuilder: BoolQueryBuilder = boolQuery()
    params foreach { case (key, value) => queryBuilder.must(termQuery(key, value)) }
    queryBuilder
  }


}

