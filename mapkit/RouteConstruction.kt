package com.example.test1

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.RequestPoint
import com.yandex.mapkit.RequestPointType
import com.yandex.mapkit.directions.DirectionsFactory
import com.yandex.mapkit.directions.driving.DrivingOptions
import com.yandex.mapkit.directions.driving.DrivingRoute
import com.yandex.mapkit.directions.driving.DrivingRouter
import com.yandex.mapkit.directions.driving.DrivingSession
import com.yandex.mapkit.directions.driving.VehicleOptions
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraListener
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.CameraUpdateReason
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.map.MapObjectCollection
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.Error
import com.yandex.mapkit.search.SearchFactory
import com.yandex.mapkit.search.SearchManager
import com.yandex.mapkit.search.Session
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import android.util.Log
import com.yandex.mapkit.geometry.Geometry
import com.yandex.mapkit.search.Response
import com.yandex.mapkit.search.SearchManagerType
import com.yandex.mapkit.search.SearchOptions
import com.yandex.runtime.network.NetworkError
import com.yandex.runtime.network.RemoteError
import java.io.IOException

class Places : ArrayList<PlacesToItem>()

data class PlacesToItem(
    val entries: List<Entry>,
    val id: String
)

data class Entry(
    val destpoint: Destpoint,
    val entryno: Int,
    val maxdistance: String,
    val mindistance: String,
    val startpoint: String,
    val time: String,
    val tsys: String
)

data class Destpoint(
    val category: String,
    val name: String
)

class MainActivity : AppCompatActivity(), CameraListener, DrivingSession.DrivingRouteListener, Session.SearchListener {
    private lateinit var mapview: MapView
    private lateinit var searchEdit: EditText
    private val startLocation: Point = Point(60.004942, 30.197075)
    private var endLocation: Point = Point(59.996546, 30.201452)

    private var mapObjects: MapObjectCollection? = null
    private var drivingRouter: DrivingRouter? = null
    private var drivingSession:DrivingSession? = null
    private lateinit var searchManager: SearchManager
    private lateinit var searchSession: Session

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.setApiKey("1c7699cf-6a79-4ac0-8155-5c4e8311e11b")
        MapKitFactory.initialize(this)
        setContentView(R.layout.activity_main)

        searchManager = SearchFactory.getInstance().createSearchManager(SearchManagerType.COMBINED)
        val searchOptions = SearchOptions()
        searchSession = searchManager.submit(
            "Вкусвилл",
            Geometry.fromPoint(Point(59.998583, 30.199898)),
            searchOptions,
            this
        )

        mapview = findViewById(R.id.mapview)
        mapview.mapWindow.map.move(
        CameraPosition(Point(59.999750, 30.199028), 15.0f, 0.0f,0.0f),
        Animation(Animation.Type.SMOOTH, 10f), null)
        drivingRouter = DirectionsFactory.getInstance().createDrivingRouter()
        mapObjects = mapview.mapWindow.map.mapObjects.addCollection()
        //convertJSON()
    }

    private fun convertJSON() {
        val jsonFileString = getJsonDataFromAsset(applicationContext, "test_json.json")
        Log.i("data", jsonFileString!!)

        val gson = Gson()
        val listPlaces = object : TypeToken<List<Places>>() {}.type

        val places: List<Places> = gson.fromJson(jsonFileString, listPlaces)
        places.forEachIndexed { idx, place -> Log.i("data", "> Item $idx:\n$place") }
    }

    private fun getJsonDataFromAsset(context: Context, fileName: String): String? {
        val jsonString: String
        try {
            jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }

    private fun submitQuery(text: String) {
        TODO("Not yet implemented")
    }


    private fun submitRequest() {
        val drivingOptions = DrivingOptions()
        val vehicleOptions = VehicleOptions()
        val requestPoints:ArrayList<RequestPoint> = ArrayList()
        requestPoints.add(RequestPoint(startLocation, RequestPointType.WAYPOINT, null, null))
        requestPoints.add(RequestPoint(endLocation, RequestPointType.WAYPOINT, null, null))
        drivingSession = drivingRouter!!.requestRoutes(requestPoints, drivingOptions, vehicleOptions, this)
    }

    override fun onStop() {
        mapview.onStop()
        MapKitFactory.getInstance().onStop()
        super.onStop()
    }

    override fun onStart() {
        mapview.onStart()
        MapKitFactory.getInstance().onStart()
        super.onStart()
    }


    override fun onCameraPositionChanged(
        map: Map,
        cameraPosition: CameraPosition,
        cameraUpdateReason: CameraUpdateReason,
        finished: Boolean
    ) {
        if (finished) {
            submitQuery(searchEdit.text.toString())
        }
    }

    override fun onDrivingRoutes(p0: MutableList<DrivingRoute>) {
        for (route in p0) {
            mapObjects!!.addPolyline(route.geometry)
        }
    }

    override fun onDrivingRoutesError(p0: Error) {
        val errorMessage = "Unknown error"
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
    }

    override fun onSearchResponse(response: Response) {
        val firstResult = response.collection.children.firstOrNull()

        if (firstResult != null) {
            val coordinates = firstResult.obj!!.geometry[0].point
            endLocation = Point(coordinates!!.latitude, coordinates.longitude)
        } else {
            val errorMessage = "Places not found"
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
        }
        submitRequest()
    }

    override fun onSearchError(error: Error) {
        var errorMessage = "Unknown error"
        if (error is RemoteError) {
            errorMessage = "Wireless error"
        } else if (error is NetworkError) {
            errorMessage = "Problem with internet"
        }
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
    }
}