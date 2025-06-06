package com.bonial.codechallenge.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.bonial.codechallenge.data.repositpry.advertisement.model.BrochureContent
import com.bonial.codechallenge.data.repositpry.advertisement.model.ContentType
import com.bonial.codechallenge.data.repositpry.advertisement.model.ContentItem

@Composable
fun BrochureGrid(brochures: List<ContentItem>) {

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            count = brochures.size,
            key = { index ->
                brochures[index].content?.id ?: ""
            }, // Unique key per item
            span = { index ->
                if (brochures[index].contentType == ContentType.BROCHURE_PREMIUM) {
                    GridItemSpan(2) // Premium items take full width
                } else {
                    GridItemSpan(1) // Normal items take half width
                }
            },
            contentType = { index ->
                brochures[index].contentType
            },
        ) { index ->
            BrochureItem(brochure = (brochures[index].content as BrochureContent))
        }
    }
}

@Composable
fun BrochureItem(brochure: BrochureContent, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val imageUrl = brochure.brochureImage
            if (imageUrl != null) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = "Brochure Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.Gray),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No Image", color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = brochure.publisher?.name ?: "-",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}

// Utility function to make `brochurePremium` full-width
fun Modifier.gridSpan(span: Int) = this.then(
    Modifier.layout { measurable, constraints ->
        val placeable = measurable.measure(constraints)
        layout(placeable.width * span, placeable.height) { placeable.placeRelative(0, 0) }
    }
)
