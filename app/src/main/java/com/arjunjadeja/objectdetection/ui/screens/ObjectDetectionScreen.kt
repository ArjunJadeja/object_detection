package com.arjunjadeja.objectdetection.ui.screens

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.arjunjadeja.objectdetection.R
import com.arjunjadeja.objectdetection.ui.MainViewModel
import com.arjunjadeja.objectdetection.ui.base.Dimens
import com.arjunjadeja.objectdetection.ui.base.Strings
import com.arjunjadeja.objectdetection.ui.base.UiState

@Composable
fun ObjectDetectionScreen(
    mainViewModel: MainViewModel,
    navController: NavController
) {
    val originalImageState by mainViewModel.selectedImage.collectAsState()
    val detectedImageState by mainViewModel.detectedObjectImage.collectAsState()

    Scaffold(
        topBar = { TopAppBar(onBackPressed = { navController.navigateUp() }) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LoadImage(imageState = originalImageState, labelText = Strings.ORIGINAL_IMAGE)

            Spacer(modifier = Modifier.height(Dimens.bigSpace))

            LoadImage(imageState = detectedImageState, labelText = Strings.PROCESSED_IMAGE)
        }
    }
}

@Composable
fun LoadImage(
    imageState: UiState<Bitmap>,
    labelText: String
) {
    when (imageState) {
        is UiState.Success -> {
            val imageBitmap = imageState.data.asImageBitmap()
            ImageLayout(labelText = labelText, imageBitmap = imageBitmap)
        }

        is UiState.Error -> {
            Text(text = imageState.message, color = MaterialTheme.colorScheme.error)
        }

        UiState.Loading -> {
            CircularProgressIndicator()
        }
    }
}

@Composable
private fun ImageLayout(labelText: String, imageBitmap: ImageBitmap) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Dimens.mediumPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            bitmap = imageBitmap,
            contentDescription = labelText,
            modifier = Modifier
                .size(Dimens.imageSize)
                .border(1.dp, MaterialTheme.colorScheme.secondary, MaterialTheme.shapes.medium)
                .clip(MaterialTheme.shapes.medium),
            contentScale = ContentScale.FillBounds
        )
        Spacer(modifier = Modifier.height(Dimens.mediumSpace))
        Text(text = labelText, style = MaterialTheme.typography.labelLarge)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopAppBar(onBackPressed: () -> Unit) {
    TopAppBar(
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BackButton { onBackPressed() }

                Text(
                    modifier = Modifier
                        .padding(start = Dimens.mediumPadding)
                        .weight(1f),
                    text = Strings.APP_NAME
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
        )
    )
}

@Composable
private fun BackButton(onClick: () -> Unit) {
    Icon(
        modifier = Modifier
            .padding(Dimens.smallPadding)
            .clickable { onClick() },
        painter = painterResource(id = R.drawable.back_icon),
        contentDescription = Strings.BACK_BUTTON
    )
}