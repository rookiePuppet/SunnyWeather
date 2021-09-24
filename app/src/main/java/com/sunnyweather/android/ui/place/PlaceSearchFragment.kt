package com.sunnyweather.android.ui.place

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.*
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sunnyweather.android.R

class PlaceSearchFragment : Fragment() {

    private val placeSearchViewModel by lazy { ViewModelProvider(this).get(PlaceSearchViewModel::class.java) }

    val placeSavedViewModel: PlaceSavedViewModel by activityViewModels()

    private lateinit var adapter: PlaceSearchAdapter
    private lateinit var placeSearchRecyclerView: RecyclerView
    private lateinit var placeEditText: EditText
    private lateinit var bgImageView: ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_place_search, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        placeEditText = requireActivity().findViewById(R.id.placeEditText)
        placeSearchRecyclerView = requireActivity().findViewById(R.id.placeSearchRecyclerView)
        bgImageView = requireActivity().findViewById(R.id.bgImageView)

        val layoutManager = LinearLayoutManager(activity)
        placeSearchRecyclerView.layoutManager = layoutManager
        adapter = PlaceSearchAdapter(this, placeSearchViewModel.placeList)
        placeSearchRecyclerView.adapter = adapter
        placeEditText.addTextChangedListener { editable ->
            val content = editable.toString()
            if(content.isNotEmpty()) {
                placeSearchViewModel.searchPlaces(content)
            } else {
                placeSearchRecyclerView.visibility = View.GONE
                bgImageView.visibility = View.VISIBLE
                placeSearchViewModel.placeList.clear()
                adapter.notifyDataSetChanged()
            }
        }
        placeSearchViewModel.placeLiveData.observe(viewLifecycleOwner) { result ->
            val places = result.getOrNull()
            if (places != null) {
                placeSearchRecyclerView.visibility = View.VISIBLE
                bgImageView.visibility = View.GONE
                placeSearchViewModel.placeList.clear()
                placeSearchViewModel.placeList.addAll(places)
                adapter.notifyDataSetChanged()
            } else {
                Toast.makeText(activity, R.string.cannotSearchTip, Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
        }

    }

}