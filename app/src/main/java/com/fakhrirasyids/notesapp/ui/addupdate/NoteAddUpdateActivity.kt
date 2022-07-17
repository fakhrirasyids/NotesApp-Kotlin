package com.fakhrirasyids.notesapp.ui.addupdate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.fakhrirasyids.notesapp.R
import com.fakhrirasyids.notesapp.database.NoteEntity
import com.fakhrirasyids.notesapp.databinding.ActivityNoteAddUpdateBinding
import com.fakhrirasyids.notesapp.ui.factory.ViewModelFactory
import com.fakhrirasyids.notesapp.ui.main.MainActivity
import java.text.SimpleDateFormat
import java.util.*

class NoteAddUpdateActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNoteAddUpdateBinding
    private lateinit var noteAddUpdateViewModel: NoteAddUpdateViewModel
    private var note: NoteEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoteAddUpdateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        noteAddUpdateViewModel = obtainViewModel(this@NoteAddUpdateActivity)

        Toast.makeText(this, "INI EXTRANYA ${intent.getStringExtra(ID_NOTE)}", Toast.LENGTH_SHORT)
            .show()

        val noteId: String? = intent.getStringExtra(ID_NOTE)

        if (noteId.isNullOrBlank()) {
            binding.apply {
                tvInfo.text = "Add Note"
                btnDelete.visibility = View.GONE
                clearEdtText()

                binding.fabCheck.apply {
                    setOnClickListener {
                        val date = "Added at ${getCurrentDate()}"
                        note = NoteEntity(
                            title = binding.edtTitle.text.toString(),
                            description = binding.edtDescription.text.toString(),
                            date = date
                        )
                        noteAddUpdateViewModel.insert(note!!)

                        val iMainAdd = Intent(this@NoteAddUpdateActivity, MainActivity::class.java)
                        setResult(RESULT_ADD, iMainAdd)
                        finish()
                    }
                }
            }
        } else {
            val id = noteId.toInt()
            binding.tvInfo.text = "Update Note"
            binding.btnDelete.visibility = View.VISIBLE

            noteAddUpdateViewModel.getNoteById(id)
                .observe(this@NoteAddUpdateActivity) {
                    if (it == null) {
                        clearEdtText()
                    } else {
                        setNoteValue(it)
                    }
                }

            binding.btnDelete.setOnClickListener {
                showAlertDialog(ALERT_DIALOG_DELETE, id)
            }


            binding.fabCheck.apply {
                setOnClickListener {
                    val title = binding.edtTitle.text.toString()
                    val description = binding.edtDescription.text.toString()
                    val date = "Updated at ${getCurrentDate()}"

                    noteAddUpdateViewModel.update(title, description, date, id)

                    val iMainUpdate = Intent(this@NoteAddUpdateActivity, MainActivity::class.java)
                    setResult(RESULT_UPDATE, iMainUpdate)
                    finish()
                }
            }
        }
    }

    private fun clearEdtText() {
        binding.apply {
            edtTitle.text = null
            tvItemDate.visibility = View.GONE
            tvItemDate.text = null
            edtDescription.text = null
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity): NoteAddUpdateViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[NoteAddUpdateViewModel::class.java]
    }

    private fun setNoteValue(note: NoteEntity) {
        binding.apply {
            edtTitle.setText(note.title)
            tvItemDate.visibility = View.VISIBLE
            tvItemDate.text = note.date
            edtDescription.setText(note.description)
        }
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())
        val date = Date()
        return dateFormat.format(date)
    }

    override fun onBackPressed() {
        showAlertDialog(ALERT_DIALOG_CLOSE, null)
    }

    private fun showAlertDialog(type: Int, id: Int?) {
        val isDialogClose = type == ALERT_DIALOG_CLOSE
        val dialogTitle: String
        val dialogMessage: String
        if (isDialogClose) {
            dialogTitle = "Cancel"
            dialogMessage = "Do you want to cancel changes to the note?"
        } else {
            dialogTitle = "Delete Note"
            dialogMessage = "Do you want to delete this note?"
        }
        val alertDialogBuilder = AlertDialog.Builder(this, R.style.MyDialogTheme)
        alertDialogBuilder.setTitle(dialogTitle)
        alertDialogBuilder
            .setMessage(dialogMessage)
            .setCancelable(false)
            .setPositiveButton("Yes") { _, _ ->
                if (isDialogClose) {
                    finish()
                } else {
                    noteAddUpdateViewModel.delete(id!!)

                    val iMainDelete = Intent(this, MainActivity::class.java)
                    setResult(RESULT_DELETE, iMainDelete)
                    finish()
                }
            }
            .setNegativeButton("No") { dialog, _ -> dialog.cancel() }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    companion object {
        const val RESULT_ADD = 101
        const val RESULT_UPDATE = 201
        const val RESULT_DELETE = 301
        const val ALERT_DIALOG_CLOSE = 10
        const val ALERT_DIALOG_DELETE = 20
        const val ID_NOTE = "0"
    }
}