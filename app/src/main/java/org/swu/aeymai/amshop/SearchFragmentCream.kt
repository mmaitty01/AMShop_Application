package org.swu.aeymai.amshop


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import org.swu.aeymai.amshop.Adapter.SearchAdapter
import org.swu.aeymai.amshop.Adapter.SearchOnlineItem
import org.swu.aeymai.amshop.Repository.FirestoreRepository

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragmentBook.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchFragmentCraem : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var count: Int = 0

    private lateinit var SearchRecyclerView: RecyclerView
    private var SearchList: MutableList<SearchOnlineItem> = mutableListOf<SearchOnlineItem>()
    private var firebaseRepository = FirestoreRepository()
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipeRefreshLayout = view.findViewById<SwipeRefreshLayout>(R.id.swipeHomeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener {
            fetchCategoriesDataFromDatabase()
        }
        SearchRecyclerView = view.findViewById<RecyclerView>(R.id.recyclerView);
        fetchCategoriesDataFromDatabase()
        var etSearchInput = view.findViewById<EditText>(R.id.etSearchInput);
        var cat = "อาหารสด"
        etSearchInput.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                if (etSearchInput.text.length > 0) {
                    var filteredItem = SearchList.filter {
                        it.namemark.toLowerCase().contains(etSearchInput.text.toString().toLowerCase()) or
                                it.nameprod.toLowerCase().contains(etSearchInput.text.toString().toLowerCase())
                    }

                    SearchRecyclerView.adapter = SearchAdapter(filteredItem)
                } else {

                    var cat = SearchList.filter {
                        it.category.toLowerCase().contains(etSearchInput.text.toString().toLowerCase())
                    }

                    SearchRecyclerView.adapter = SearchAdapter(cat)
                }

            }
        })
    }



    private fun fetchCategoriesDataFromDatabase() {
        firebaseRepository.getSavedSearch().get().addOnSuccessListener { documents ->
            SearchList.clear()
            for (document in documents) {
                SearchList.add(document.toObject(SearchOnlineItem::class.java))
            }
            var cate = "สกินแคร์"
            var cat1 = SearchList.filter {
                it.category.toLowerCase().contains(cate)
            }

            SearchRecyclerView.adapter = SearchAdapter(cat1)
            // hide pull loading
            swipeRefreshLayout.isRefreshing = false
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SearchFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SearchFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}


