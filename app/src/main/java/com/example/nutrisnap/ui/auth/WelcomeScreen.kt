
package com.example.nutrisnap.ui.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nutrisnap.R
import androidx.compose.ui.tooling.preview.Preview
import com.example.nutrisnap.ui.theme.NutriSnapTheme


@Composable
fun WelcomeScreen(
    onGetStartedClick: () -> Unit
) {
    // Compose layout that allows us to stack elements on top of one another
    Box(
        modifier = Modifier.fillMaxSize() //tells the box to occupy all the available screen space
    ) {

        // Full-screen food background
        Image(
            // painterResource() - loads an image from Android resources
            painter = painterResource(
                id = R.drawable.food_background
            ),
            contentDescription = "NutriSnap food background",
            modifier = Modifier.fillMaxSize(), // Image fills the screen
            contentScale = ContentScale.Crop //Fills the area while cropping excess image portions if necessary
        )

        // Dark overlay to make text readable
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.30f))
        )

        // Top branding and bottom button
        // Column arranges elements vertically, one below another.
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp, vertical = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            // Top section
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "NutriSnap",
                    fontSize = 55.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Snap · Scan · Know",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }

            // Bottom button
            Button(
                onClick = onGetStartedClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE94E0B),
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Get Started",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
// To preview the screen
//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun WelcomeScreenPreview() {
//    NutriSnapTheme {
//        WelcomeScreen(
//            onGetStartedClick = {}
//        )
//    }
//}
