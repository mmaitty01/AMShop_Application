package org.swu.aeymai.amshop

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import org.swu.aeymai.amshop.Adapter.PopularAdapter
import org.swu.aeymai.amshop.Adapter.PopularViewItem
import org.swu.aeymai.amshop.Adapter.PromotionAdapter

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var promotionPager: ViewPager2
    private lateinit var popularRecyclerView: RecyclerView
    private lateinit var popularImageList: MutableList<PopularViewItem>

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
        return inflater.inflate(R.layout.fragment_home, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val promoImageList = mutableListOf<Int>()
        promoImageList.add(R.drawable.promo1)
        promoImageList.add(R.drawable.promo22)
        promoImageList.add(R.drawable.promo3)
        promoImageList.add(R.drawable.promo4)
        promoImageList.add(R.drawable.promo5)
        promotionPager = view.findViewById<ViewPager2>(R.id.promotionView);
        promotionPager.adapter = PromotionAdapter(promoImageList);

        var viewAllCategoryText = view.findViewById<ImageView>(R.id.sport);
        viewAllCategoryText.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.apply {
                replace(R.id.fl_container, SearchFragmentSport())
                    .addToBackStack("HomeFragment")
                    .commit()
            }
        }

        var viewAllCategoryText1 = view.findViewById<ImageView>(R.id.book);
        viewAllCategoryText1.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.apply {
                replace(R.id.fl_container, SearchFragmentBook())
                    .addToBackStack("HomeFragment")
                    .commit()
            }
        }

        var viewAllCategoryText2 = view.findViewById<ImageView>(R.id.food);
        viewAllCategoryText2.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.apply {
                replace(R.id.fl_container, SearchFragmentFood())
                    .addToBackStack("HomeFragment")
                    .commit()
            }
        }
        var viewAllCategoryText3 = view.findViewById<ImageView>(R.id.cream);
        viewAllCategoryText3.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.apply {
                replace(R.id.fl_container, SearchFragmentCraem())
                    .addToBackStack("HomeFragment")
                    .commit()
            }
        }

        var viewAllCategoryText4 = view.findViewById<ImageView>(R.id.fruit_veg);
        viewAllCategoryText4.setOnClickListener {
            activity?.supportFragmentManager?.beginTransaction()?.apply {
                replace(R.id.fl_container, SearchFragmentFV())
                    .addToBackStack("HomeFragment")
                    .commit()
            }
        }

        popularRecyclerView = view.findViewById<RecyclerView>(R.id.recyclerView);
        popularImageList = mutableListOf<PopularViewItem>()
        popularImageList.add(PopularViewItem("Innisfree Jeju Cream", R.drawable.popular1,599))
        popularImageList.add(PopularViewItem("Lays", R.drawable.popular2,39))
        popularImageList.add(PopularViewItem("School and the City",R.drawable.popular3,299))
        popularImageList.add(PopularViewItem("Adidas Bag", R.drawable.popular4,2500))
        popularImageList.add(PopularViewItem("เห็ดเข็มทอง", R.drawable.popular5,50))
        popularRecyclerView.adapter = PopularAdapter(popularImageList);


        var etSearchInput = view.findViewById<EditText>(R.id.etSearchInput);
        etSearchInput.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                if (etSearchInput.text.length > 0) {
                    var filteredItem = popularImageList.filter {
                        it.title.toLowerCase().contains(etSearchInput.text.toString().toLowerCase())
                    }
                    popularRecyclerView.adapter = PopularAdapter(filteredItem)
                } else {
                    popularRecyclerView.adapter = PopularAdapter(popularImageList)
                }
            }
        })
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}