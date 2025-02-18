package com.example.homework

import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import android.net.Uri
import androidx.compose.ui.platform.LocalContext
import java.io.IOException

fun saveImageToInternalStorage(context: Context, uri: Uri): Uri? {
    val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
    val file = File(context.filesDir, "profile_picture.jpg") // Save with a fixed name

    return try {
        inputStream?.use { input ->
            FileOutputStream(file).use { output ->
                input.copyTo(output)
            }
        }
        Uri.fromFile(file) // Return the URI of the saved file
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}

@Composable
fun Screen2(
    navController: NavController,
    state: ChangeState,
    onEvent: (ChangeEvent) -> Unit,
) {
    val context = LocalContext.current
    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.let {
                val savedUri = saveImageToInternalStorage(context, it)
                savedUri?.let { uriString ->
                    onEvent(ChangeEvent.SetProfilePic(uriString.toString())) // Save to database
                }
            }
        }
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = {
            navController.navigate("Screen1") {
                popUpTo("Screen1") { inclusive = true }
            }
        }) {
            Text(text = "Go To Home")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            singlePhotoPickerLauncher.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }) {
            Text("Pick an Image")
        }

        Spacer(modifier = Modifier.height(16.dp))

        state.profilePic.let {
            AsyncImage(
                model = it,
                contentDescription = "Selected Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .border(2.dp, Color.Gray, RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        TextField(
            value = state.userName,
            onValueChange = { newUserName ->
                onEvent(ChangeEvent.SetUserName(newUserName))
            },
            label = { Text("Enter Username") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            navController.navigate("Screen3") {
                popUpTo("Screen3") { inclusive = true }
            }
        }) {
            Text(text = "Go To Compass")
        }
    }
}
