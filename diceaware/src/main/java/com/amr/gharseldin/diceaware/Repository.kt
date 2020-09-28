package com.amr.gharseldin.diceaware

import android.content.Context
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.security.SecureRandom
import java.util.concurrent.atomic.AtomicReference

private const val ASSET_FILENAME = "eff_short_wordlist_2_0.txt"

object Repository {

    private val wordCache = AtomicReference<List<String>>()
    private val random = SecureRandom()

    fun generate(context: Context, count: Int): List<String>{
        val words: List<String>? =  wordCache.get()
        return words?.let{rollDemBones(it, count)}?: loadAndGenerate(context, count)
    }

    private fun loadAndGenerate(
        context: Context,
        count: Int
    ): List<String> {
        val inputStream = context.assets.open((ASSET_FILENAME))

        inputStream.use{
            val words = it.readLines()
                .map{line -> line.split("\t")}
                .filter{tokens -> tokens.size==2}
                .map{tokens -> tokens[1]}
            wordCache.set(words)
            return rollDemBones(words, count)
        }
    }

    //Extension function on inputStream class
    private fun InputStream.readLines(): List<String>{
        val result = mutableListOf<String>()
        BufferedReader(InputStreamReader(this)).forEachLine {
            result.add(it)
        }
        return result;
    }

    private fun rollDemBones(words: List<String>, count: Int) =
        List(count){words[random.nextInt(words.size)]}
}