package org.swu.aeymai.amshop

import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.esri.arcgisruntime.ArcGISRuntimeEnvironment
import com.esri.arcgisruntime.geometry.*
import com.esri.arcgisruntime.mapping.ArcGISMap
import com.esri.arcgisruntime.mapping.Basemap
import com.esri.arcgisruntime.mapping.Viewpoint
import com.esri.arcgisruntime.mapping.view.DefaultMapViewOnTouchListener
import com.esri.arcgisruntime.mapping.view.Graphic
import com.esri.arcgisruntime.mapping.view.GraphicsOverlay
import com.esri.arcgisruntime.mapping.view.MapView
import com.esri.arcgisruntime.symbology.PictureMarkerSymbol
import com.esri.arcgisruntime.symbology.SimpleFillSymbol
import com.esri.arcgisruntime.symbology.SimpleLineSymbol
import com.esri.arcgisruntime.symbology.SimpleMarkerSymbol

import org.swu.aeymai.amshop.Adapter.PointAdapter
import org.swu.aeymai.amshop.Adapter.PointViewItem

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MapFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MapFragment : Fragment() {
    private lateinit var PointList: List<PointViewItem>
    private lateinit var mMapView: MapView
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var graphicLayer: GraphicsOverlay
    private lateinit var shopPointAdapter: PointAdapter
    private lateinit var shopViewPager2: ViewPager2
    private var isFirst: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        ArcGISRuntimeEnvironment.setLicense(resources.getString(R.string.arcgis_license_key))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mMapView = view.findViewById<MapView>(R.id.mapView);
        val latitude = 13.700547
        val longitude = 100.535619
        val levelOfDetail = 15
        val map = ArcGISMap(Basemap.Type.DARK_GRAY_CANVAS_VECTOR, latitude, longitude, levelOfDetail)
        mMapView.map = map
        mMapView.isAttributionTextVisible = false
        addGraphics()

        // Create List of Page
        PointList = listOf<PointViewItem>(
            PointViewItem("ร้าน B2S", "2 ถนนสายไหม ซอยสายไหม3", R.drawable.temp, genShopPoint(100.53892960569901, 13.701952685466564)),
            PointViewItem("ร้านนายอินทร์", "2/1 ถนนสายไหม ซอยสายไหม1", R.drawable.temp1, genShopPoint(100.53033429238417, 13.71200666513494)),
            PointViewItem("ร้าน Top", "31/56 ถนนสายไหม ซอยสายไหม10", R.drawable.temp2, genShopPoint(100.52811496041558, 13.695102867638928)),
            PointViewItem("ร้าน 7-11", "3/56 ถนนสายไหม ซอยสายไหม45", R.drawable.temp3, genShopPoint(100.48636850713976, 13.80197600326033))
        )


        // find Extent of all point
        val listOfPoint = mutableListOf<Point>()
        PointList.forEach {
            listOfPoint.add(it.location)
        }
        var mCompleteExtent: Envelope = GeometryEngine.combineExtents(listOfPoint);
        var newX1 = mCompleteExtent.xMin - mCompleteExtent.xMin*0.0001
        var newY1 = mCompleteExtent.yMin - mCompleteExtent.yMin*0.0001
        var newX2 = mCompleteExtent.xMax + mCompleteExtent.xMax*0.0001
        var newY2 = mCompleteExtent.yMax + mCompleteExtent.yMax*0.0001
        var mExtent2 = Envelope(newX1, newY1, newX2, newY2, mCompleteExtent.spatialReference)


        shopViewPager2 = view.findViewById<ViewPager2>(R.id.shopViewPager2);
        createBuffer7km()
        shopPointAdapter = PointAdapter(PointList)
        shopViewPager2.adapter = shopPointAdapter;
        shopViewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (isFirst) {
                    isFirst = false;
                    addCurrentLocation(mExtent2)
                } else mMapView.setViewpointCenterAsync(PointList[position].location, 20000.0)
            }
        })
    }





    private fun createBuffer7km() {
        val currentLocationPoint = Point(100.53168604697537, 13.713314244935012, SpatialReferences.getWgs84())
        // create buffer polygon
        // create buffer geometry 100 meter
        val geometryBuffer = GeometryEngine.bufferGeodetic(currentLocationPoint, 20.0,
            LinearUnit(LinearUnitId.KILOMETERS), Double.NaN, GeodeticCurveType.GEODESIC)

        // create symbol for buffer geometry
        val geodesicOutlineSymbol = SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.BLACK, 2F)
        // 0x4D00FF00 = Green Color with 25% Opacity (4D = 25%)
        val geodesicBufferFillSymbol = SimpleFillSymbol(SimpleFillSymbol.Style.SOLID,
            0x4D00FF00.toInt(), geodesicOutlineSymbol)

        // new graphic
        val graphicBuffer =  Graphic(geometryBuffer, geodesicBufferFillSymbol)
        //  graphicLayer.graphics.add(graphicBuffer)

        // filter 7km shop
        PointList = PointList.filter {
            var isInside = GeometryEngine.intersects(geometryBuffer, it.location)
            isInside
        }
        // add all filtered shop to map
        PointList.forEach {
            genShopPoint(it.location.x, it.location.y, true)
        }
    }

    private fun addCurrentLocation(extent: Envelope) {
        val currentLocationPoint = Point(100.53168604697537, 13.703314244935012, SpatialReferences.getWgs84())
        // create an opaque orange (0xFFFF5733) point symbol with a blue (0xFF0063FF) outline symbol
        val simpleMarkerSymbol = SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.YELLOW, 10f)
        val blueOutlineSymbol = SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.RED, 2f)
        simpleMarkerSymbol.outline = blueOutlineSymbol
        // create a graphic with the point geometry and symbol
        val pointGraphic = Graphic(currentLocationPoint, simpleMarkerSymbol)
        // add the point graphic to the graphics overlay
        graphicLayer.graphics.add(pointGraphic)
        // zoom to current location point
        mMapView.setViewpointAsync(Viewpoint(extent));
    }



    private fun genShopPoint(x: Double, y: Double, addToMap: Boolean = false): Point {
        // create a point geometry with a location and spatial reference
        // Point(latitude, longitude, spatial reference)
        val point = Point(x, y, SpatialReferences.getWgs84())
        if (addToMap) {
            // code for  get image in drawable folder
            val pictureMarkerSymbol = PictureMarkerSymbol.createAsync(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.mk
                ) as BitmapDrawable
            ).get()

            // set width, height, z from ground
            pictureMarkerSymbol.height = 52f
            pictureMarkerSymbol.width = 52f
            pictureMarkerSymbol.offsetY = 11f
            // create a graphic with the point geometry and symbol
            val pointGraphic = Graphic(point, pictureMarkerSymbol)

            // add the point graphic to the graphics overlay
            graphicLayer.graphics.add(pointGraphic)
        }
        return point
    }



    private fun addPoint() {
        // create a point geometry with a location and spatial reference
        // Point(latitude, longitude, spatial reference)
        val point = Point(100.53892960569901, 13.701952685466564, SpatialReferences.getWgs84())

        // create an opaque orange (0xFFFF5733) point symbol with a blue (0xFF0063FF) outline symbol
        val simpleMarkerSymbol = SimpleMarkerSymbol(SimpleMarkerSymbol.Style.CIRCLE, Color.YELLOW, 10f)
        val blueOutlineSymbol = SimpleLineSymbol(SimpleLineSymbol.Style.SOLID, Color.RED, 2f)
        simpleMarkerSymbol.outline = blueOutlineSymbol
        // create a graphic with the point geometry and symbol
        val pointGraphic = Graphic(point, simpleMarkerSymbol)

        // add the point graphic to the graphics overlay
        graphicLayer.graphics.add(pointGraphic)
    }

    private fun addGraphics() {
        // create a graphics overlay and add it to the map view
        graphicLayer = GraphicsOverlay()
        mMapView.graphicsOverlays.add(graphicLayer)
    }

    override fun onPause() {
        super.onPause()
        mMapView.pause()
    }

    override fun onResume() {
        super.onResume()
        mMapView.resume()
    }

    override fun onDestroy() {
        super.onDestroy()
        mMapView.dispose()
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MapFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MapFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}