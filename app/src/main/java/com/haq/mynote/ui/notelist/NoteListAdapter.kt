package com.haq.mynote.ui.notelist

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.haq.mynote.R
import com.haq.mynote.getString
import com.haq.mynote.ui.TimeFormatter
import kotlinx.android.synthetic.main.note_item_layout.view.*

class NoteListAdapter : RecyclerView.Adapter<NoteListAdapter.NoteItemViewHolder>() {

    private var listNote = mutableListOf<NoteUIModel>()

    private val internalListener = object : OnNoteItemClickListener {
        override fun invoke(item: NoteUIModel) {
            listener?.let { it(item) }
        }
    }

    var listener: OnNoteItemClickListener? = null

    fun setData(data: List<NoteUIModel>?) {
        listNote.clear()
        if (!data.isNullOrEmpty()) {
            listNote.addAll(data)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): NoteItemViewHolder {
        return NoteItemViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.note_item_layout, parent, false),
            internalListener
        )
    }

    override fun getItemCount(): Int = listNote.size

    override fun onBindViewHolder(holder: NoteItemViewHolder, position: Int) {
        if (0 <= position && position < listNote.size) {
            holder.bindView(listNote[position])
        }
    }

    class NoteItemViewHolder(itemView: View, clickListener: OnNoteItemClickListener) :
        RecyclerView.ViewHolder(itemView) {

        private lateinit var data: NoteUIModel

        init {
            itemView.setOnClickListener {
                clickListener(data)
            }
        }

        fun bindView(data: NoteUIModel) {
            this.data = data
            itemView.tvTitle.text = if (data.title.isNotEmpty()) data.title else getString(R.string.empty_title)
            itemView.tvSnippet.text = data.content
            itemView.tvTime.text = TimeFormatter.shortFormat(data.createTime)
            itemView.setBackgroundResource(if (data.isSelected) R.color.colorSelectedNote else R.color.colorPrimary)
        }
    }
}

private typealias OnNoteItemClickListener = (NoteUIModel) -> Unit