package com.mambo.iris.sensors

import android.content.Context
import android.hardware.*

class GyroController(ctx:Context, private val onMatrix:(FloatArray)->Unit):SensorEventListener{
  private val sm=ctx.getSystemService(Context.SENSOR_SERVICE) as SensorManager
  private val rv=sm.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
  private val m=FloatArray(16)
  fun start(){ rv?.let{ sm.registerListener(this,it,SensorManager.SENSOR_DELAY_GAME) } }
  fun stop(){ sm.unregisterListener(this) }
  override fun onSensorChanged(e:SensorEvent){ if(e.sensor.type==Sensor.TYPE_ROTATION_VECTOR){ SensorManager.getRotationMatrixFromVector(m,e.values); onMatrix(m) } }
  override fun onAccuracyChanged(s:Sensor?, a:Int) {}
}