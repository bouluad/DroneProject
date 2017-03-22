package istic.fr.droneproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import istic.fr.droneproject.service.impl.DronePositionServiceImpl;

public class TestServiceDrone extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_service_drone);
        DronePositionServiceImpl d=new DronePositionServiceImpl();
        d.getDronePositionwithRetrofit();

    }
}
