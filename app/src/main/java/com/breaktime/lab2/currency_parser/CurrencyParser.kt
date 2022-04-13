package com.breaktime.lab2.currency_parser

import android.util.Log
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import javax.inject.Inject
import kotlin.math.round

class CurrencyParser @Inject constructor() {
    var data = HashMap<String, Pair<Double, Double>>()

    fun update() {
        val document: Document

        try {
            document = Jsoup.connect("https://myfin.by/currency/minsk").get()
        } catch (e: Exception) {
            Log.d("ERROR", "update: ${e.message}")
            return
        }
        val answer = document.body().html()
        var index = answer.indexOf("best: 'a:13:{s:3:")
        val page = answer.substring(index + 13, index + 198)
        index = page.indexOf("\"")
        val resList = mutableListOf<String>()
        while (index != -1) {
            val endIndex = page.indexOf("\"", index + 1)
            resList += page.substring(index + 1, endIndex)
            index = page.indexOf("\"", endIndex + 1)
        }
        data[resList[0]] = resList[2].toDouble() to resList[4].toDouble()
        data[resList[5]] = resList[7].toDouble() to resList[9].toDouble()
        var buy = resList[12].toDouble()
        buy *= 100000
        buy = round(buy)
        buy /= 1000f
        var sell = resList[14].toDouble()
        sell *= 100000
        sell = round(sell)
        sell /= 1000f
        data[resList[10]] = buy to sell
    }
}