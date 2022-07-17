package com.fakhrirasyids.notesapp.ui.main

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.fakhrirasyids.notesapp.data.NoteEntity
import com.fakhrirasyids.notesapp.databinding.ActivityMainBinding
import com.fakhrirasyids.notesapp.ui.adapter.NoteAdapter
import com.fakhrirasyids.notesapp.ui.adapter.OnItemClickCallback
import com.fakhrirasyids.notesapp.ui.addupdate.NoteAddUpdateActivity
import com.fakhrirasyids.notesapp.ui.addupdate.NoteAddUpdateActivity.Companion.ID_NOTE
import com.fakhrirasyids.notesapp.ui.settings.SettingsActivity
import com.fakhrirasyids.notesapp.utils.ViewModelFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    val resultLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.data != null) {
            when (result.resultCode) {
                NoteAddUpdateActivity.RESULT_ADD -> {
                    Toast.makeText(this, "Note successfully added!", Toast.LENGTH_SHORT).show()
                }
                NoteAddUpdateActivity.RESULT_UPDATE -> {
                    Toast.makeText(this, "Note successfully updated!", Toast.LENGTH_SHORT).show()
                }
                NoteAddUpdateActivity.RESULT_DELETE -> {
                    Toast.makeText(this, "Note successfully deleted!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setNotesLayout()

        mainViewModel = obtainViewModel(this@MainActivity)

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        binding.svSearchNote.clearFocus()

        notesObserver()
        searchedNoteObserver()
        setUpSearchView()

        binding.btnSettings.setOnClickListener {
            val iSettings = Intent(this, SettingsActivity::class.java)
            startActivity(iSettings)
        }

        binding.fabAdd.setOnClickListener {
            val iAdd = Intent(this@MainActivity, NoteAddUpdateActivity::class.java)
            resultLauncher.launch(iAdd)
        }
    }

    private fun setNotesLayout() {
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.rvNotes.layoutManager = layoutManager
        binding.rvNotes.setHasFixedSize(true)
    }

    private fun setUpSearchView() {
        with(binding) {
            svSearchNote.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    mainViewModel.setQuery(query)
                    getSearchedNotesObserver(query)
                    return true
                }

                override fun onQueryTextChange(query: String?): Boolean {
                    if (query.isNullOrBlank()) {
                        notesObserver()
                    }
                    return false
                }
            })
        }
    }

    private fun searchedNoteObserver() {
        mainViewModel.searchedQuery.observe(this) {
            if (it != null) {
                getSearchedNotesObserver(it)
            } else {
                notesObserver()
            }
        }
    }

    private fun notesObserver() {
        mainViewModel.getAllNotes().observe(this@MainActivity) {
            setAllNoteList(it)
        }
    }

    private fun getSearchedNotesObserver(query: String?) {
        if (!query.isNullOrBlank()) {
            mainViewModel.getSearchedNotes(query).observe(this@MainActivity) {
                if (it.isEmpty()) {
                    Toast.makeText(
                        this@MainActivity,
                        "No notes Found!",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    setAllNoteList(it)
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setAllNoteList(noteList: List<NoteEntity>) {
        val adapter = NoteAdapter(noteList)
        adapter.notifyDataSetChanged()
        binding.rvNotes.adapter = adapter
        adapter.setOnItemClickCallback(object : OnItemClickCallback {
            override fun onItemClicked(id: Int) {
                val iView = Intent(this@MainActivity, NoteAddUpdateActivity::class.java)
                iView.putExtra(ID_NOTE, id.toString())
                resultLauncher.launch(iView)
            }
        })
    }

    private fun obtainViewModel(activity: AppCompatActivity): MainViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[MainViewModel::class.java]
    }
}