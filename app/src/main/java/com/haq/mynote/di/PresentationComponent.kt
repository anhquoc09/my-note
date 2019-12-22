package com.haq.mynote.di

import com.haq.mynote.ui.notelist.NoteListFragment
import dagger.Subcomponent

@PresentationScope
@Subcomponent
interface PresentationComponent {
    fun inject(fragment: NoteListFragment)
}