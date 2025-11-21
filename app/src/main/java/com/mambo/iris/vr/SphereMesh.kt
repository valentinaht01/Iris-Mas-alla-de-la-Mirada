package com.mambo.iris.vr

import android.opengl.GLES20
import java.nio.*
import kotlin.math.*

class SphereMesh(seg:Int=32){
  private val vb:FloatBuffer; private val tb:FloatBuffer; private val count:Int
  init{
    val verts=ArrayList<Float>(); val uvs=ArrayList<Float>(); val rings=seg; val secs=seg
    for(r in 0..rings){ val v=r/rings.toFloat(); val th=v*PI; val st=sin(th).toFloat(); val ct=cos(th).toFloat()
      for(s in 0..secs){ val u=s/secs.toFloat(); val ph=u*2f*PI.toFloat(); val x=st*cos(ph); val y=ct; val z=st*sin(ph); verts.add(-x); verts.add(y); verts.add(z); uvs.add(u); uvs.add(1-v) } }
    val idx=ArrayList<Int>(); for(r in 0 until rings){ for(s in 0 until secs){ val i1=r*(secs+1)+s; val i2=i1+secs+1; idx.add(i1); idx.add(i2); idx.add(i1+1); idx.add(i1+1); idx.add(i2); idx.add(i2+1) } }
    val vArr=FloatArray(idx.size*3); val tArr=FloatArray(idx.size*2); var vi=0; var ti=0
    for(i in idx){ vArr[vi++]=verts[i*3]; vArr[vi++]=verts[i*3+1]; vArr[vi++]=verts[i*3+2]; tArr[ti++]=uvs[i*2]; tArr[ti++]=uvs[i*2+1] }
    vb=ByteBuffer.allocateDirect(vArr.size*4).order(ByteOrder.nativeOrder()).asFloatBuffer().put(vArr); vb.position(0)
    tb=ByteBuffer.allocateDirect(tArr.size*4).order(ByteOrder.nativeOrder()).asFloatBuffer().put(tArr); tb.position(0)
    count=vArr.size
  }
  fun draw(p:Int){
    val aP=GLES20.glGetAttribLocation(p,"aPosition"); val aT=GLES20.glGetAttribLocation(p,"aTexCoord")
    GLES20.glEnableVertexAttribArray(aP); GLES20.glVertexAttribPointer(aP,3,GLES20.GL_FLOAT,false,0,vb)
    GLES20.glEnableVertexAttribArray(aT); GLES20.glVertexAttribPointer(aT,2,GLES20.GL_FLOAT,false,0,tb)
    GLES20.glDrawArrays(GLES20.GL_TRIANGLES,0,count)
    GLES20.glDisableVertexAttribArray(aP); GLES20.glDisableVertexAttribArray(aT)
  }
}