package com.mambo.iris.vr

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.ar.core.Anchor
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import com.google.ar.sceneform.ux.TransformableNode
import com.mambo.iris.R

class ArExperienceActivity : AppCompatActivity() {

    private lateinit var arFragment: ArFragment
    private var modelRenderable: ModelRenderable? = null

    companion object {
        // Extras para decidir qué modelo y qué título usar
        const val EXTRA_MODEL_PATH = "extra_model_path"
        const val EXTRA_TITLE = "extra_title"

        private const val TAG = "ArExperienceActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ar_experience)

        // === TOOLBAR ===
        val toolbar = findViewById<MaterialToolbar?>(R.id.ar_toolbar)
        toolbar?.apply {
            setNavigationIcon(androidx.appcompat.R.drawable.abc_ic_ab_back_material)
            setNavigationOnClickListener {
                finish()
            }

            // Si viene un título por Intent, úsalo
            val customTitle = intent.getStringExtra(EXTRA_TITLE)
            if (!customTitle.isNullOrBlank()) {
                title = customTitle
            } else {
                title = "Experiencia AR"
            }
        }

        // === AR FRAGMENT EN EL CONTENEDOR ===
        val existingFragment = supportFragmentManager.findFragmentById(R.id.ar_fragment_container)
        arFragment = if (existingFragment is ArFragment) {
            existingFragment
        } else {
            val newFragment = ArFragment()
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.ar_fragment_container, newFragment)
                .commitNow()
            newFragment
        }

        // Configurar carga del modelo y tap en el plano
        setupArModel()
    }

    private fun setupArModel() {
        // 1. Definir qué modelo cargar:
        //    - Si viene por Intent → usarlo.
        //    - Si no viene → usar zootropo por defecto.
        val modelPathFromIntent = intent.getStringExtra(EXTRA_MODEL_PATH)
        val modelPath = modelPathFromIntent ?: "models/zootropo.glb"

        Log.d(TAG, "Cargando modelo desde: $modelPath")
        val uri = Uri.parse(modelPath)

        ModelRenderable.builder()
            .setSource(this, uri)
            .setIsFilamentGltf(true)
            .build()
            .thenAccept { renderable ->
                modelRenderable = renderable
                Log.d(TAG, "Modelo $modelPath cargado correctamente")
                Toast.makeText(this, "Modelo AR cargado ✅", Toast.LENGTH_SHORT).show()
            }
            .exceptionally { throwable ->
                Log.e(TAG, "Error al cargar $modelPath: ${throwable.message}", throwable)
                Toast.makeText(
                    this,
                    "Error al cargar modelo: ${throwable.message}",
                    Toast.LENGTH_LONG
                ).show()
                null
            }

        // 2. Tap en un plano para colocar el modelo
        arFragment.setOnTapArPlaneListener { hitResult, plane, motionEvent ->
            Toast.makeText(this, "Toque detectado sobre plano 👆", Toast.LENGTH_SHORT).show()
            Log.d(TAG, "Tap en plano detectado")

            val renderable = modelRenderable
            if (renderable == null) {
                Log.w(TAG, "Se intentó colocar el modelo antes de que terminara de cargar")
                Toast.makeText(this, "Espera, aún se está cargando el modelo…", Toast.LENGTH_SHORT)
                    .show()
                return@setOnTapArPlaneListener
            }

            // Crear un Anchor donde tocaste
            val anchor: Anchor = hitResult.createAnchor()
            val anchorNode = AnchorNode(anchor).apply {
                setParent(arFragment.arSceneView.scene)
            }

            // Crear nodo transformable con el modelo
            val node = TransformableNode(arFragment.transformationSystem).apply {
                this.renderable = renderable

                // Escala grande para que se vea bien (ajustable)
                scaleController.minScale = 0.5f
                scaleController.maxScale = 1.5f
                localScale = Vector3(0.8f, 0.8f, 0.8f)

                // Levantarlo un poco sobre el plano
                localPosition = Vector3(0f, 0.1f, 0f)

                setParent(anchorNode)
                select()
            }

            Log.d(TAG, "Modelo colocado en el plano")
            Toast.makeText(this, "Modelo colocado 🎉", Toast.LENGTH_SHORT).show()
        }
    }
}





















