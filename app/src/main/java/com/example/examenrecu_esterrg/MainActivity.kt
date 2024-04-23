package com.example.examenrecu_esterrg

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.examenrecu_esterrg.ui.theme.ExamenRecu_EsterRGTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ExamenRecu_EsterRGTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Pantalla()
                }
            }
        }
    }
}

@Composable
fun Pantalla(modifier: Modifier = Modifier, listaAsignatura: ArrayList<Asignatura> = DataSource.asignaturas){

    val textoUltimaAccion: MutableState<String> = remember { mutableStateOf("Última acción:" + "\nNo has hecho ninguna acción.") }
    val textoResumen: MutableState<String> = remember { mutableStateOf("Resumen:" + "\nNo hay nada que mostrar.") }

   Column () {
       AsignaturasYBotones(modifier = modifier,
           asignaturas = listaAsignatura,
           textoUltimaAccion = textoUltimaAccion,
           textoResumen = textoResumen
       )
   }
}


@OptIn(ExperimentalMaterial3Api::class) //hace falta para el Textfield
@Composable
private fun AsignaturasYBotones (modifier: Modifier, asignaturas: ArrayList<Asignatura>,
                                 textoUltimaAccion: MutableState<String>, textoResumen: MutableState<String>){

    var valorHorasNuevo by remember { mutableStateOf("")}

    Column (modifier = Modifier.fillMaxWidth()){
            Text(
                text = "Bienvenido a la academia de Ester Rivero Goldero",
                modifier = Modifier
                    .weight(0.07f)
                    .fillMaxWidth()
                    .background(Color.LightGray)
                    .padding(10.dp)
            )
            LazyVerticalGrid(
                modifier = Modifier.weight(0.40f),
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.Center,
                horizontalArrangement = Arrangement.Center
            ){
                items(asignaturas){ asignatura ->
                    Card (modifier = Modifier.padding(5.dp)){
                        Text(text = "Asig: ${asignatura.nombre}",
                            modifier = Modifier
                                .background(Color.Yellow)
                                .padding(15.dp)
                                .fillMaxWidth())
                        Text(text = "€/hora: ${asignatura.precioHora}",
                            modifier = Modifier
                                .background(Color.Cyan)
                                .padding(15.dp)
                                .fillMaxWidth())
                        Row (modifier = Modifier
                            .align(alignment = CenterHorizontally)) {
                            Button(onClick = {
                                sumaHoras(asignatura, valorHorasNuevo, textoUltimaAccion, textoResumen, asignaturas)
                            }) {
                                Text(text = "+")
                            }
                            Button(onClick = {
                                restarHoras(asignatura, valorHorasNuevo, textoUltimaAccion, textoResumen, asignaturas)
                            }) {
                                Text(text = "-")
                            }
                        }
                    }
                }
            }

            TextField(value = valorHorasNuevo,
                onValueChange = { nuevoValor ->
                    valorHorasNuevo = nuevoValor },
                label= { Text(text = "Horas a contratar o eliminar")},
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next),
                modifier = Modifier
                    .weight(0.15f)
                    .fillMaxWidth()
                    .padding(15.dp))

            Column (modifier = Modifier.weight(0.50f).background(Color.LightGray)
                .fillMaxWidth()
                .padding(20.dp)){
                Text(
                    text = "${textoUltimaAccion.value}",
                    modifier = Modifier
                        .background(Color.Magenta)
                        .padding(10.dp)
                        .align(alignment = CenterHorizontally)
                )
                Text(
                    text = "${textoResumen.value}",
                    modifier = Modifier
                        .background(Color.White)
                        .padding(10.dp)
                        .align(alignment = CenterHorizontally)
                )
            }
    }
}

fun sumaHoras (asignatura: Asignatura, anadirHoras: String, textoUltimaAccion: MutableState<String>,
               textoResumen: MutableState<String>, asignaturas: ArrayList<Asignatura>) {
    var anadirHorasFin = anadirHoras.toInt()

    if (anadirHorasFin > 0) {
        asignatura.recuentoHoras += anadirHorasFin
        textoUltimaAccion.value =
            "Última acción:" + "\nSe han añadido $anadirHorasFin horas a ${asignatura.nombre} a un precio ${asignatura.precioHora}."
        textoResumen.value = "Resumen: "
        for (asig in asignaturas){
            if(asig.recuentoHoras > 0){
                textoResumen.value +=
                    "\nAsig: ${asig.nombre} / Precio hora: ${asig.precioHora} / Horas: ${asig.recuentoHoras}"
            }

        }
    }
}

fun restarHoras (asignatura: Asignatura, restarHoras: String, textoUltimaAccion: MutableState<String>,
                 textoResumen: MutableState<String>, asignaturas: ArrayList<Asignatura>) {
    var restarHorasFin = restarHoras.toInt()

    if (restarHorasFin > 0) {
        textoResumen.value = "Resumen: "
        if(asignatura.recuentoHoras >= restarHorasFin){
            asignatura.recuentoHoras -= restarHorasFin
        }else {
            restarHorasFin = restarHorasFin - asignatura.recuentoHoras
            asignatura.recuentoHoras = 0
        }
        textoUltimaAccion.value =
            "Última acción:" + "\nSe han restado $restarHorasFin horas a ${asignatura.nombre} a un precio ${asignatura.precioHora}."
        for(asig in asignaturas){
            if(asig.recuentoHoras > 0){
                textoResumen.value +=
                    "\nAsig: ${asig.nombre} / Precio hora: ${asig.precioHora} / Horas: ${asig.recuentoHoras}"
            }
        }
    }
}


