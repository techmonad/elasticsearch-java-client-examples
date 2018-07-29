package com.techmonad.main


import com.techmonad.es.{CatalogueRepository, ESConfig}


object MainApp extends App {


  val catalogueRepo = new CatalogueRepository with ESConfig




}
