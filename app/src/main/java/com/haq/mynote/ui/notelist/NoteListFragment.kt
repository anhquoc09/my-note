package com.haq.mynote.ui.notelist

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearLayoutManager.VERTICAL
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import com.haq.mynote.R
import com.haq.mynote.ViewModelFactory
import com.haq.mynote.di.Injector
import com.haq.mynote.showShortToast
import com.haq.mynote.ui.BaseFragment
import com.haq.mynote.ui.TimeFormatter
import kotlinx.android.synthetic.main.fragment_note_list.*
import javax.inject.Inject

class NoteListFragment : BaseFragment() {

    private lateinit var viewModel: NoteListViewModel
    private lateinit var adapter: NoteListAdapter
    private var pressedTimeMillis = 0L

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injectComponent()
        initViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_note_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar()
        initListNoteRecyclerView()
        initContent()
        observeViewModel()
    }

    override fun onPause() {
        viewModel.saveNote()
        super.onPause()
    }

    override fun onBackPressed(): Boolean {
        val currentTimeMillis = System.currentTimeMillis()
        if (currentTimeMillis - pressedTimeMillis <= 2000) {
            return false
        }
        showShortToast(getString(R.string.pressed_back_to_exit))
        pressedTimeMillis = currentTimeMillis
        return true
    }

    private fun injectComponent() {
        Injector.instance.appComponent.plusPresentationComponent().inject(this)
    }

    private fun initViewModel() {
        viewModel = ViewModelProviders
            .of(this, viewModelFactory)
            .get(NoteListViewModel::class.java)
    }

    private fun initToolbar() {
        imgList.setOnClickListener {
            if (noteListContainer.visibility == VISIBLE) {
                noteListContainer.visibility = GONE
            } else {
                noteListContainer.visibility = VISIBLE
            }
        }
        imgDelete.setOnClickListener { showConfirmDialog() }
        imgNew.setOnClickListener { viewModel.newNote() }
        imgClear.setOnClickListener { edtSearch.setText("") }

        edtSearch.imeOptions = EditorInfo.IME_ACTION_DONE
        edtSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hideKeyboard(requireActivity())
                edtSearch.clearFocus()
                true
            } else false
        }
        edtSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                imgClear.visibility = if (s.isNullOrEmpty()) GONE else VISIBLE
                viewModel.filter(s.toString())
            }
        })
        edtSearch.setOnTouchListener { _, _ ->
            if (noteListContainer.visibility != VISIBLE) {
                noteListContainer.visibility = VISIBLE
            }
            false
        }
    }

    private fun initListNoteRecyclerView() {
        adapter = NoteListAdapter()
        adapter.listener = { note -> onItemClick(note) }
        rvListNote.adapter = adapter
        rvListNote.layoutManager = LinearLayoutManager(context, VERTICAL, false)
    }

    private fun initContent() {
        edtTitle.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                viewModel.updateTitle(s.toString())
            }
        })

        edtContent.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                viewModel.updateContent(s.toString())
            }
        })
    }

    private fun observeViewModel() {
        viewModel.state.observe(viewLifecycleOwner, Observer { it?.let { render(it) } })
    }

    private fun render(noteListSate: NotesState) {
        renderList(noteListSate)
        renderDetail(noteListSate)
        if (noteListSate.error != null) {
            showShortToast(noteListSate.error.message.toString())
        }
    }

    private fun renderList(noteListSate: NotesState) {
        progressLayout.visibility = if (noteListSate.isLoading) VISIBLE else GONE
        adapter.setData(noteListSate.noteList)
        tvEmpty.visibility = if (noteListSate.noteList.isNullOrEmpty()) VISIBLE else GONE
    }

    private fun renderDetail(noteListSate: NotesState) {
        noteListSate.selectedNote?.let {
            tvCreateTime.visibility = VISIBLE
            tvCreateTime.text = TimeFormatter.fullFormat(it.createTime)
        } ?: let { tvCreateTime.visibility = GONE }
        edtTitle.setText(noteListSate.selectedNote?.title.orEmpty())
        edtContent.setText(noteListSate.selectedNote?.content.orEmpty())
        imgDelete.visibility = if (noteListSate.selectedNote != null) VISIBLE else GONE
    }

    private fun onItemClick(note: NoteUIModel) {
        hideKeyboard(requireActivity())
        edtSearch.clearFocus()
        viewModel.selectNote(note.id)
    }

    private fun hideKeyboard(activity: Activity) {
        try {
            val inputMethodManager =
                activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            val view = activity.currentFocus ?: return
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        } catch (e: Exception) {
            showShortToast(e.message.toString())
        }
    }

    private fun showConfirmDialog() {
        context?.let {
            val dialogBuilder = AlertDialog.Builder(it).apply {
                setMessage(getString(R.string.confirm_delete_message))
                setCancelable(false)
                setPositiveButton(getString(R.string.confirm_delete_yes)) { _, _ -> viewModel.deleteSelectedNote() }
                setNegativeButton(getString(R.string.confirm_delete_no)) { dialog, _ -> dialog.cancel() }
            }
            val alert = dialogBuilder.create()
            alert.setTitle(getString(R.string.confirm_delete_title))
            alert.setCancelable(true)
            alert.show()
        }
    }
}