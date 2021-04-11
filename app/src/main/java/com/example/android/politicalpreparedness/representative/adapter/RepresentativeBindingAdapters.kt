package com.example.android.politicalpreparedness.representative.adapter

import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.model.Election
import com.example.android.politicalpreparedness.representative.model.Representative
import timber.log.Timber

@BindingAdapter("profileImage")
fun fetchImage(view: ImageView, src: String?) {
    src?.let {
        val uri = src.toUri().buildUpon().scheme("https").build()
        //TODO: Add Glide call to load image and circle crop - user ic_profile as a placeholder and for errors.
        Glide.with(view.context).load(uri).placeholder(R.drawable.ic_profile).transform(CircleCrop()).into(view)
    }
}

@BindingAdapter("stateValue")
fun Spinner.setNewValue(value: String?) {
    val adapter = toTypedAdapter<String>(this.adapter as ArrayAdapter<*>)
    val position = when (adapter.getItem(0)) {
        is String -> adapter.getPosition(value)
        else -> this.selectedItemPosition
    }
    if (position >= 0) {
        setSelection(position)
    }
}

@BindingAdapter("representativesList")
fun bindRepresentativesList(recyclerView: RecyclerView, representativesList: List<Representative>?) {
    val adapter: RepresentativeListAdapter = recyclerView.adapter as RepresentativeListAdapter
    Timber.i("$representativesList")
    adapter.submitList(representativesList)

}

inline fun <reified T> toTypedAdapter(adapter: ArrayAdapter<*>): ArrayAdapter<T> {
    return adapter as ArrayAdapter<T>
}

@BindingAdapter("followedElectionsList")
fun bindFollowedElectionList(recyclerView: RecyclerView, followedElectionList: List<Election>?) {
    val adapter: ElectionListAdapter = recyclerView.adapter as ElectionListAdapter
    adapter.submitList(followedElectionList)
}

@BindingAdapter("upcomingElectionList")
fun bindUpcomingElectionList(recyclerView: RecyclerView, upcomingElectionList: List<Election>?) {
    val adapter: ElectionListAdapter = recyclerView.adapter as ElectionListAdapter
    adapter.submitList(upcomingElectionList)
}
