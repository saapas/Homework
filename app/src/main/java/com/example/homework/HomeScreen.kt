package com.example.homework

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.homework.ui.theme.HomeWorkTheme

data class Message(val author: String, val body: String)

@Composable
fun MessageCard(msg: Message, username: String, image: String) {
    Row(modifier = Modifier.padding(all = 8.dp)) {
        image.let {
            AsyncImage(
                model = it, // URI of the selected image
                placeholder = painterResource(R.drawable.n_ytt_kuva_2024_11_06_134830),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(50.dp) // Fixed size for the profile picture
                    .clip(RoundedCornerShape(12.dp)) // Rounded corners
                    .border(1.dp, Color.Gray, RoundedCornerShape(12.dp)), // Optional border
                contentScale = ContentScale.Crop // Crop the image to fit the size
            )
            Spacer(modifier = Modifier.width(8.dp))
        }
        var isExpanded by remember { mutableStateOf(false) }
        // surfaceColor will be updated gradually from one color to the other
        val surfaceColor by animateColorAsState(
            if (isExpanded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
        )
        // We toggle the isExpanded variable when we click on this Column
        Column(modifier = Modifier.clickable { isExpanded = !isExpanded }) {
            Text(
                text = username,
                color = MaterialTheme.colorScheme.secondary
            )
            Spacer(modifier = Modifier.height(4.dp))

            Surface(
                shape = MaterialTheme.shapes.medium,
                shadowElevation = 1.dp,
                // surfaceColor color will be changing gradually from primary to surface
                color = surfaceColor,
                // animateContentSize will change the Surface size gradually
                modifier = Modifier
                    .animateContentSize()
                    .padding(1.dp)
            ) {
                Text(
                    text = msg.body,
                    modifier = Modifier.padding(all = 4.dp),
                    // If the message is expanded, we display all its content
                    // otherwise we only display the first line
                    maxLines = if (isExpanded) Int.MAX_VALUE else 1,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Composable
fun Conversation(messages: List<Message>, username: String, image: String) {
    LazyColumn {
        items(messages) { message ->
            MessageCard(message, username, image)
        }
    }
}

@Composable
fun HomeScreen(
    messageData: SampleData,
    navController: NavController,
    state: ChangeState
) {
    HomeWorkTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Conversation(messageData.conversationSample, state.userName, state.profilePic)
            SettingButton(navController)
        }
    }
}

@Composable
fun SettingButton(navController: NavController) {
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.End
    ) {
        Spacer(modifier = Modifier.height(15.dp))
        Button(
            onClick = {
                navController.navigate("Screen2")
            },
            colors = ButtonColors(
                MaterialTheme.colorScheme.surfaceContainer,
                MaterialTheme.colorScheme.onBackground,
                MaterialTheme.colorScheme.secondary,
                MaterialTheme.colorScheme.surface
            )
        ) {
            Text(
                text = "Go To Screen2",
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
