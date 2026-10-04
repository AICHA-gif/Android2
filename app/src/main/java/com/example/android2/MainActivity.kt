package com.example.android2

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var editTextPoids: EditText
    private lateinit var editTextTaille: EditText
    private lateinit var buttonCalculer: Button
    private lateinit var buttonEffacer: Button
    private lateinit var textViewImc: TextView
    private lateinit var textViewCategorie: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editTextPoids = findViewById(R.id.editTextPoids)
        editTextTaille = findViewById(R.id.editTextTaille)
        buttonCalculer = findViewById(R.id.buttonCalculer)
        buttonEffacer = findViewById(R.id.buttonEffacer)
        textViewImc = findViewById(R.id.textViewImc)
        textViewCategorie = findViewById(R.id.textViewCategorie)

        buttonCalculer.setOnClickListener { calculerImc() }
        buttonEffacer.setOnClickListener { effacer() }
    }

    private fun calculerImc() {
        val poidsTexte = editTextPoids.text.toString().trim()
        val tailleTexte = editTextTaille.text.toString().trim()

        // Vérifier que les champs sont renseignés
        if (poidsTexte.isEmpty() || tailleTexte.isEmpty()) {
            if (poidsTexte.isEmpty()) editTextPoids.error = getString(R.string.erreur_champ_vide)
            if (tailleTexte.isEmpty()) editTextTaille.error = getString(R.string.erreur_champ_vide)
            Toast.makeText(this, R.string.toast_completer, Toast.LENGTH_SHORT).show()
            reinitialiserResultat()
            return
        }

        // Conversion (la virgule est acceptée)
        val poids = poidsTexte.replace(',', '.').toDoubleOrNull()
        val taille = tailleTexte.replace(',', '.').toDoubleOrNull()

        if (poids == null) {
            editTextPoids.error = getString(R.string.erreur_nombre_invalide)
        }
        if (taille == null) {
            editTextTaille.error = getString(R.string.erreur_nombre_invalide)
        }
        if (poids == null || taille == null) {
            reinitialiserResultat()
            return
        }

        // Valeurs strictement positives
        if (poids <= 0 || taille <= 0) {
            if (poids <= 0) editTextPoids.error = getString(R.string.erreur_valeur_positive)
            if (taille <= 0) editTextTaille.error = getString(R.string.erreur_valeur_positive)
            reinitialiserResultat()
            return
        }

        // Calcul et arrondi à 2 décimales
        val imc = Math.round((poids / (taille * taille)) * 100) / 100.0

        // Catégorie et couleur
        val categorie: Int
        val couleur: Int
        if (imc < 18.5) {
            categorie = R.string.cat_insuffisance
            couleur = R.color.imc_orange
        } else if (imc < 25) {
            categorie = R.string.cat_normale
            couleur = R.color.imc_vert
        } else if (imc < 30) {
            categorie = R.string.cat_surpoids
            couleur = R.color.imc_orange
        } else if (imc < 35) {
            categorie = R.string.cat_obesite_moderee
            couleur = R.color.imc_rouge
        } else if (imc < 40) {
            categorie = R.string.cat_obesite_severe
            couleur = R.color.imc_rouge
        } else {
            categorie = R.string.cat_obesite_morbide
            couleur = R.color.imc_rouge_fonce
        }

        val couleurResolue = ContextCompat.getColor(this, couleur)
        textViewImc.text = getString(R.string.imc_valeur, imc)
        textViewCategorie.text = getString(categorie)
        textViewImc.setTextColor(couleurResolue)
        textViewCategorie.setTextColor(couleurResolue)
    }

    private fun effacer() {
        editTextPoids.text.clear()
        editTextTaille.text.clear()
        editTextPoids.error = null
        editTextTaille.error = null
        reinitialiserResultat()
        editTextPoids.requestFocus()
    }

    private fun reinitialiserResultat() {
        textViewImc.text = ""
        textViewCategorie.text = ""
    }
}