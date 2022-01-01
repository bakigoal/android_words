package com.example.wordsapp.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.Button
import androidx.annotation.LayoutRes
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.wordsapp.DetailActivity
import com.example.wordsapp.R

class WordAdapter(private val letterId: String, context: Context) :
    RecyclerView.Adapter<WordAdapter.WordViewHolder>() {

    private val filteredWords: List<String>

    init {
        val words = context.resources.getStringArray(R.array.words).toList()

        filteredWords = words
            .filter { it.startsWith(letterId, ignoreCase = true) }
            .shuffled()
            .take(5)
            .sorted()
    }

    class WordViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val button: Button = view.findViewById(R.id.button_item)

        companion object {
            @LayoutRes
            const val LAYOUT = R.layout.item_view
        }
    }

    override fun getItemCount(): Int = filteredWords.size

    /**
     * Creates new views with R.layout.item_view as its template
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val layout = LayoutInflater.from(parent.context)
            .inflate(WordViewHolder.LAYOUT, parent, false)

        // Setup custom accessibility delegate to set the text read
        layout.accessibilityDelegate = Accessibility
        return WordViewHolder(layout)
    }

    /**
     * Replaces the content of an existing view with new data
     */
    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val item = filteredWords[position]
        val context = holder.view.context
        holder.button.text = item
        holder.button.setOnClickListener {
            val queryUrl: Uri = Uri.parse("${DetailActivity.SEARCH_PREFIX}$item")
            val intent = Intent(Intent.ACTION_VIEW, queryUrl)
            context.startActivity(intent)
        }
    }

    // Setup custom accessibility delegate to set the text read with
    // an accessibility service
    companion object Accessibility : View.AccessibilityDelegate() {
        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun onInitializeAccessibilityNodeInfo(host: View?, info: AccessibilityNodeInfo?) {
            super.onInitializeAccessibilityNodeInfo(host, info)
            // With `null` as the second argument to [AccessibilityAction], the
            // accessibility service announces "double tap to activate".
            // If a custom string is provided,
            // it announces "double tap to <custom string>".
            info?.addAction(
                AccessibilityNodeInfo.AccessibilityAction(
                    AccessibilityNodeInfo.ACTION_CLICK,
                    host?.context?.getString(R.string.look_up_word)
                )
            )
        }
    }
}