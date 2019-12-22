package com.haq.mynote.ui.notelist

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
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
import com.haq.mynote.KeyboardUtils
import com.haq.mynote.R
import com.haq.mynote.ViewModelFactory
import com.haq.mynote.di.Injector
import com.haq.mynote.showShortToast
import com.haq.mynote.ui.AnimationUtil
import com.haq.mynote.ui.BaseFragment
import kotlinx.android.synthetic.main.fragment_note_list.*
import javax.inject.Inject

class NoteListFragment : BaseFragment() {

    private lateinit var viewModel: NoteListViewModel
    private lateinit var adapter: NoteListAdapter

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
        observeViewModel()
    }

    override fun onBackPressed(): Boolean = false

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
                AnimationUtil.collapse(noteListContainer)
            } else {
                AnimationUtil.expand(noteListContainer)
            }
        }
        imgDelete.setOnClickListener { viewModel.deleteSelectedNote() }
        imgNew.setOnClickListener { }
        imgClear.setOnClickListener { edtSearch.setText("") }

        edtSearch.imeOptions = EditorInfo.IME_ACTION_DONE
        edtSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                KeyboardUtils.hideKeyboard(requireActivity())
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
            if (noteListContainer.visibility != VISIBLE) AnimationUtil.expand(
                noteListContainer
            )
            false
        }
    }

    private fun initListNoteRecyclerView() {
        adapter = NoteListAdapter()
        adapter.listener = { note, position -> onItemClick(note, position) }
        rvListNote.adapter = adapter
        rvListNote.layoutManager = LinearLayoutManager(context, VERTICAL, false)
    }

    private fun observeViewModel() {
        viewModel.state.observe(viewLifecycleOwner, Observer { it?.let { render(it) } })
    }

    private fun render(noteListSate: NotesState) {
        progressLayout.visibility = if (noteListSate.isLoading) VISIBLE else GONE
        adapter.setData(noteListSate.noteList)
        tvEmpty.visibility = if (noteListSate.noteList.isNullOrEmpty()) VISIBLE else GONE
        imgDelete.visibility = if (noteListSate.noteSelected != null) VISIBLE else GONE
        if (noteListSate.error != null) {
            showShortToast(noteListSate.error.message.toString())
        }
    }

    private fun onItemClick(note: NoteUIModel, position: Int) {
        KeyboardUtils.hideKeyboard(requireActivity())
        edtSearch.clearFocus()
        viewModel.selectNote(note.id)
    }
}