package com.bonial.codechallenge.ui.home.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.bonial.codechallenge.data.repositpry.advertisement.model.ContentType
import com.bonial.codechallenge.ui.model.BrochureUiModel


@Composable
fun BrochureGrid(brochures: List<BrochureUiModel>, columns: Int) {

    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        contentPadding = PaddingValues(12.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        items(
            count = brochures.size,
            key = { index ->
                brochures[index].id
            },
            span = { index ->
                if (brochures[index].type == ContentType.BROCHURE_PREMIUM) {
                    GridItemSpan(columns) // Premium items take full width dynamically
                } else {
                    GridItemSpan(1) // Normal items take single cell
                }
            },
            contentType = { index ->
                brochures[index].type
            },
        ) { index ->
            BrochureItem(brochure = brochures[index])
        }
    }
}


@Composable
fun BrochureItem(
    brochure: BrochureUiModel,
    modifier: Modifier = Modifier
) {
    var isLiked by remember { mutableStateOf(brochure.isFavourite) }

    Column(
        modifier = modifier.width(180.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = brochure.retailerName,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.W900,
                    fontSize = 18.sp
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            Icon(
                imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = "Favourite",
                tint = if (isLiked) Color.Red else Color.Gray,
                modifier = Modifier
                    .padding(4.dp)
                    .size(22.dp)
                    .clickable { isLiked = !isLiked }
                    .testTag(if (isLiked) "tag_liked" else "tag_unliked")
            )
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .border(
                width = 0.5.dp,
                color = Color.Gray,
                shape = RoundedCornerShape(12.dp)
            ),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(4.dp),

        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(brochure.imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(0.7f)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop,
                    loading = {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.LightGray.copy(alpha = 0.3f)),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.primary,
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    },
                    error = {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(0.7f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.LightGray),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "No Image",
                                tint = Color.Gray,
                                modifier = Modifier.size(64.dp)
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            Text(
                                text = "No image available",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.DarkGray,
                            )
                        }


                    }
                )

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = brochure.formattedExpiry,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White,
                            modifier = Modifier
                                .background(Color(0xAA000000), RoundedCornerShape(4.dp))
                                .padding(horizontal = 4.dp, vertical = 2.dp)
                        )
                        Text(
                            text = brochure.formattedDistance,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White,
                            modifier = Modifier
                                .background(Color(0xAA000000), RoundedCornerShape(4.dp))
                                .padding(horizontal = 2.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 300, heightDp = 600)
@Composable
fun BrochureItemNoImagePreview() {
    BrochureItem(
        brochure = BrochureUiModel(
            id = 0,
            retailerName = "Sample Store",
            imageUrl = "https://content-media.bonial.biz/fake-link/test.jpg",
            formattedExpiry = "Expires in 3 days",
            formattedDistance = "2.5 km",
            isFavourite = false,
            type = ContentType.BROCHURE
        ),
        modifier = Modifier.padding(8.dp)
    )
}

@Preview(showBackground = true, widthDp = 300, heightDp = 600)
@Composable
fun BrochureItemPreview() {
    BrochureItem(
        brochure = BrochureUiModel(
            id = 0,
            retailerName = "Sample Store",
            imageUrl = "https://content-media.bonial.biz/dce762e9-6b09-4a2d-b768-19bc12466d74/preview.jpg",
            formattedExpiry = "Expires in 8 days",
            formattedDistance = "3.5 km",
            isFavourite = false,
            type = ContentType.BROCHURE
        ),
        modifier = Modifier.padding(8.dp)
    )
}

