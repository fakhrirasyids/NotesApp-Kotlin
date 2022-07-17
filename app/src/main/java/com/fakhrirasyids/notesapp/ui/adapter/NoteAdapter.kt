package com.fakhrirasyids.notesapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fakhrirasyids.notesapp.databinding.ItemNoteBinding
import com.fakhrirasyids.notesapp.data.NoteEntity

class NoteAdapter(private var listNotes: List<NoteEntity>) :
    RecyclerView.Adapter<NoteAdapter.ViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listNotes[position])
        holder.itemView.setOnClickListener {
            onItemClickCallback.onItemClicked(listNotes[position].id)
        }
    }

    override fun getItemCount(): Int = listNotes.size

    inner class ViewHolder(private var binding: ItemNoteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(note: NoteEntity) {
            binding.tvItemTitle.text = note.title
            binding.tvItemDate.text = note.date
            binding.tvItemDescription.text = note.description
        }
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }
}