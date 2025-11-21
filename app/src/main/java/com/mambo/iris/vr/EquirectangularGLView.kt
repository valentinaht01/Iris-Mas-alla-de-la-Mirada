package com.mambo.iris.vr

import android.content.Context
import android.graphics.Bitmap
import android.opengl.GLSurfaceView

class EquirectangularGLView(ctx:Context, bmp:Bitmap):GLSurfaceView(ctx){
  private val r=SphereRenderer(bmp)
  init{ setEGLContextClientVersion(2); setRenderer(r); renderMode=RENDERMODE_CONTINUOUSLY }
  fun setOrientationMatrix(m:FloatArray){ r.setViewRotation(m) }
}