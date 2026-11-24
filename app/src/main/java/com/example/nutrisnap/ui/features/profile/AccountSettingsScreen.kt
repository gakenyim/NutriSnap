package com.example.nutrisnap.ui.features.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import com.google.firebase.auth.userProfileChangeRequest
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

private val BrandOrange = Color(0xFFFF7A00)
private val BrandOrangeSoft = Color(0xFFFFF3E8)
private val CanvasBackground = Color(0xFFF8F9FA)
private val DarkText = Color(0xFF141414)
private val GreyText = Color(0xFF6B7280)
private val CardBorder = Color(0xFFECEEF2)

@Composable
fun AccountSettingsScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {}
) {
    val auth = remember { FirebaseAuth.getInstance() }
    val currentUser = auth.currentUser
    val scope = rememberCoroutineScope()

    var name by remember { mutableStateOf(currentUser?.displayName.orEmpty()) }
    var email by remember { mutableStateOf(currentUser?.email.orEmpty()) }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var isSaving by remember { mutableStateOf(false) }
    var statusMessage by remember { mutableStateOf<String?>(null) }
    var isError by remember { mutableStateOf(false) }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = CanvasBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Top Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = DarkText
                    )
                }

                Column {
                    Text(
                        text = "Account Settings",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkText
                    )
                    Text(
                        text = "Manage your profile and security",
                        fontSize = 12.sp,
                        color = GreyText
                    )
                }
            }

            // Status Banner (Success / Error feedback)
            statusMessage?.let { message ->
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    color = if (isError) Color(0xFFFEE2E2) else Color(0xFFDCFCE7),
                    border = BorderStroke(
                        1.dp,
                        if (isError) Color(0xFFFCA5A5) else Color(0xFF86EFAC)
                    )
                ) {
                    Text(
                        text = message,
                        modifier = Modifier.padding(14.dp),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (isError) Color(0xFFB91C1C) else Color(0xFF15803D)
                    )
                }
            }

            // Personal Information Card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                color = Color.White,
                border = BorderStroke(1.dp, CardBorder),
                shadowElevation = 2.dp
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Personal Information",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkText
                    )

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Full Name", fontSize = 12.sp) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Person,
                                contentDescription = null,
                                tint = GreyText
                            )
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BrandOrange,
                            unfocusedBorderColor = CardBorder
                        )
                    )

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Email Address", fontSize = 12.sp) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Email,
                                contentDescription = null,
                                tint = GreyText
                            )
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BrandOrange,
                            unfocusedBorderColor = CardBorder
                        )
                    )
                }
            }

            // Security / Change Password Card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                color = Color.White,
                border = BorderStroke(1.dp, CardBorder),
                shadowElevation = 2.dp
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Change Password",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkText
                    )

                    OutlinedTextField(
                        value = newPassword,
                        onValueChange = { newPassword = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("New Password (optional)", fontSize = 12.sp) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Lock,
                                contentDescription = null,
                                tint = GreyText
                            )
                        },
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = BrandOrange,
                            unfocusedBorderColor = CardBorder
                        )
                    )

                    if (newPassword.isNotEmpty()) {
                        OutlinedTextField(
                            value = confirmPassword,
                            onValueChange = { confirmPassword = it },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Confirm New Password", fontSize = 12.sp) },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Outlined.Lock,
                                    contentDescription = null,
                                    tint = GreyText
                                )
                            },
                            visualTransformation = PasswordVisualTransformation(),
                            singleLine = true,
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = BrandOrange,
                                unfocusedBorderColor = CardBorder
                            )
                        )
                    }
                }
            }

            // Save Changes Button
            Button(
                enabled = !isSaving,
                onClick = {
                    if (currentUser == null) {
                        isError = true
                        statusMessage = "No signed-in user found."
                        return@Button
                    }

                    if (newPassword.isNotEmpty() && newPassword != confirmPassword) {
                        isError = true
                        statusMessage = "Passwords do not match."
                        return@Button
                    }

                    if (newPassword.isNotEmpty() && newPassword.length < 6) {
                        isError = true
                        statusMessage = "Password must be at least 6 characters."
                        return@Button
                    }

                    scope.launch {
                        isSaving = true
                        statusMessage = null
                        try {
                            val trimmedName = name.trim()
                            if (trimmedName.isNotEmpty() && trimmedName != currentUser.displayName) {
                                val profileUpdates = userProfileChangeRequest {
                                    displayName = trimmedName
                                }
                                currentUser.updateProfile(profileUpdates).await()
                            }

                            val trimmedEmail = email.trim()
                            var emailVerificationSent = false
                            if (trimmedEmail.isNotEmpty() && trimmedEmail != currentUser.email) {
                                currentUser.verifyBeforeUpdateEmail(trimmedEmail).await()
                                emailVerificationSent = true
                            }

                            if (newPassword.isNotEmpty()) {
                                currentUser.updatePassword(newPassword).await()
                                newPassword = ""
                                confirmPassword = ""
                            }

                            isError = false
                            statusMessage = if (emailVerificationSent) {
                                "Saved! Check $trimmedEmail to verify your new email address."
                            } else {
                                "Account settings updated successfully."
                            }
                        } catch (e: FirebaseAuthRecentLoginRequiredException) {
                            isError = true
                            statusMessage = "For security, please log out and log back in before changing your email or password."
                        } catch (e: Exception) {
                            isError = true
                            statusMessage = e.localizedMessage ?: "Failed to update account settings."
                        } finally {
                            isSaving = false
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BrandOrange,
                    contentColor = Color.White
                )
            ) {
                if (isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "Save Changes",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Log Out Button
            OutlinedButton(
                onClick = {
                    auth.signOut()
                    onLogoutClick()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, Color(0xFFFCA5A5)),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color(0xFFDC2626)
                )
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Log Out",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(96.dp))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun AccountSettingsScreenPreview() {
    AccountSettingsScreen()
}