package com.example.wordsapp.adapter

import android.content.Intent
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

class LetterAdapter : RecyclerView.Adapter<LetterAdapter.LetterViewHolder>() {

    private val list = ('A').rangeTo('Z').toList()

    /**
     * Provides a reference for the views needed to display items in your list.
     */
    class LetterViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val button: Button = view.findViewById(R.id.button_item)

        companion object {
            @LayoutRes
            const val LAYOUT = R.layout.item_view
        }
    }

    override fun getItemCount(): Int = list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LetterViewHolder {
        val layout = LayoutInflater.from(parent.context)
            .inflate(LetterViewHolder.LAYOUT, parent, false)
        // Setup custom accessibility delegate to set the text read
        layout.accessibilityDelegate = Accessibility
        return LetterViewHolder(layout)
    }

    /**
     * Replaces the content of an existing view with new data
     */
    override fun onBindViewHolder(holder: LetterViewHolder, position: Int) {
        val item = list[position]
        holder.button.text = item.toString()
        holder.button.setOnClickListener {
            val context = holder.view.context
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(DetailActivity.INTENT_EXTRA_LETTER, holder.button.text.toString())
            context.startActivity(intent)
        }
    }

    // Setup custom accessibility delegate to set the text read with an accessibility service
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
                    host?.context?.getString(R.string.look_up_words)
                )
            )
        }
    }
}