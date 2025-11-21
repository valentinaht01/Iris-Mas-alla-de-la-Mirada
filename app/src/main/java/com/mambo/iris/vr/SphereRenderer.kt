package com.mambo.iris.vr

import android.graphics.Bitmap
import android.opengl.*
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class SphereRenderer(private val bmp:Bitmap):GLSurfaceView.Renderer{
  private lateinit var sphere: SphereMesh; private var prog=0; private var tex=0
  private val view=FloatArray(16); private val proj=FloatArray(16); private val mvp=FloatArray(16)
  override fun onSurfaceCreated(gl:GL10?, cfg:EGLConfig?){ val vs=GLUtilsExt.compileShader(GLES20.GL_VERTEX_SHADER, V); val fs=GLUtilsExt.compileShader(GLES20.GL_FRAGMENT_SHADER, F); prog=GLUtilsExt.linkProgram(vs,fs); tex=GLUtilsExt.loadTexture(bmp); sphere=SphereMesh(32); Matrix.setIdentityM(view,0); Matrix.setIdentityM(proj,0); Matrix.setIdentityM(mvp,0) }
  override fun onSurfaceChanged(gl:GL10?, w:Int, h:Int){ GLES20.glViewport(0,0,w,h); val r=w.toFloat()/h.toFloat(); Matrix.perspectiveM(proj,0,90f,r,0.1f,100f) }
  override fun onDrawFrame(gl:GL10?){ GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT); Matrix.multiplyMM(mvp,0,proj,0,view,0); GLES20.glUseProgram(prog); val u=GLES20.glGetUniformLocation(prog,"uMVP"); GLES20.glUniformMatrix4fv(u,1,false,mvp,0); val t=GLES20.glGetUniformLocation(prog,"uTexture"); GLES20.glActiveTexture(GLES20.GL_TEXTURE0); GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, tex); GLES20.glUniform1i(t,0); sphere.draw(prog) }
  fun setViewRotation(m:FloatArray){ System.arraycopy(m,0,view,0,16) }
  companion object { private const val V="attribute vec3 aPosition;attribute vec2 aTexCoord;varying vec2 vTex;uniform mat4 uMVP;void main(){vTex=aTexCoord;gl_Position=uMVP*vec4(aPosition,1.0);}"; private const val F="precision mediump float;varying vec2 vTex;uniform sampler2D uTexture;void main(){gl_FragColor=texture2D(uTexture,vTex);}"; }
}