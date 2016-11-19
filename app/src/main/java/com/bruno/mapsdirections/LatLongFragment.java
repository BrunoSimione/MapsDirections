package com.bruno.mapsdirections;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Bruno on 17/11/2016.
 */

public class LatLongFragment extends Fragment implements View.OnClickListener{

    private EditText txtOrigem;
    private EditText txtDestino;
    private Button btnConverter;
    private Button btnTeste;

        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_latlong , container, false);

        txtOrigem = (EditText) view.findViewById(R.id.txtOrigem);
        txtDestino = (EditText) view.findViewById(R.id.txtDestino);
        btnConverter = (Button) view.findViewById(R.id.btnConverter);
        btnTeste = (Button) view.findViewById(R.id.btnTeste);


        btnConverter.setOnClickListener(this);
        btnTeste.setOnClickListener(this);

        return view;
    }

    public void onClick(View v){
        switch(v.getId()){
            case R.id.btnConverter: {

                String localorigem = txtOrigem.getText().toString();
                String localdestino = txtDestino.getText().toString();
                //callMapsAPI(localorigem,localdestino);
                Toast toast = Toast.makeText(getActivity(), "Converter",Toast.LENGTH_LONG);
                toast.show();
                break;
            }
            case R.id.btnTeste: {
                String localorigem = txtOrigem.getText().toString();
                String localdestino = txtDestino.getText().toString();
                convertAddress(localorigem,localdestino);
                //Toast toast = Toast.makeText(getActivity(), "Teste",Toast.LENGTH_LONG);
                //toast.show();
                break;
            }
        }

    }

    private void convertAddress(String origem, String destino){


        LatLng latlngOri = getLocationFromAddress(getContext(),origem);
        LatLng latlngDes = getLocationFromAddress(getContext(),destino);

        String texto1 = "Origem: Lat: " + String.valueOf(latlngOri.latitude) + " Long: " + String.valueOf(latlngOri.longitude);
        String texto2 = "Destino: Lat: " + String.valueOf(latlngDes.latitude) + " Long: " + String.valueOf(latlngDes.longitude);
        Toast toastO = Toast.makeText(getActivity(), texto1,Toast.LENGTH_SHORT);
        toastO.show();

        Toast toastD = Toast.makeText(getActivity(), texto2,Toast.LENGTH_SHORT);
        toastD.show();

        callMapsAPI(latlngOri.latitude,latlngOri.longitude,latlngDes.latitude,latlngDes.longitude);
    }

    private LatLng getLocationFromAddress(Context context, String strAddress){

        Geocoder coder = new Geocoder(context);
        List<Address> address;

        try {
            //Get latLng from String
            address = coder.getFromLocationName(strAddress,5);

            //check for null
            if (address == null) {
                return null;
            }

            //Lets take first possibility from the all possibilities.
            Address location=address.get(0);
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            return latLng;
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        return null;
    }


    private void callMapsAPI(double origemLat, double origemLong, double destinoLat, double destinoLong){
        //String url1 = "http://maps.googleapis.com/maps/api/directions/json?origin=" + origem +"&destination=" + destino + "&sensor=false";
        //String url2 ="http://www.google.com";

        StringBuilder url = new StringBuilder();
        url.append("http://maps.googleapis.com/maps/api/directions/json?origin=");
        url.append(Double.toString(origemLat));
        url.append(",");
        url.append(Double.toString(origemLong));
        url.append("&destination=");
        url.append(Double.toString(destinoLat));
        url.append(",");
        url.append(Double.toString(destinoLong));
        url.append("&sensor=false");



        JSONParser jsonParser = new JSONParser();
        try{

            //JSONObject payload = jsonParser.getJSONFromUrl(url2,null);
            //System.out.println(payload.get("content"));

            //final JSONObject json = jsonParser.getJSONFromUrl(url,null);
            final JSONObject json = jsonParser.execute(url.toString()).get();
            JSONArray routeArray = json.getJSONArray("routes");
            JSONObject routes = routeArray.getJSONObject(0);

            JSONArray newTempARr = routes.getJSONArray("legs");
            JSONObject newDisTimeOb = newTempARr.getJSONObject(0);

            JSONObject distOb = newDisTimeOb.getJSONObject("distance");
            JSONObject timeOb = newDisTimeOb.getJSONObject("duration");

            Toast toast = Toast.makeText(getActivity(), distOb.getString("text") ,Toast.LENGTH_LONG);
            toast.show();

        }catch (JSONException e){
            Toast toast = Toast.makeText(getActivity(), "Erro" ,Toast.LENGTH_LONG);
            toast.show();
        }catch(Exception e){
            Log.e("Erro1",e.toString());
        }
    }


}

