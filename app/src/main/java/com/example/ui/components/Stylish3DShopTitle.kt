package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.ShopConstants
import com.example.ui.theme.BrandGold
import com.example.ui.theme.BrandLightBlue
import com.example.ui.theme.GaladaFontFamily

/**
 * Premium 3D Bengali Shop Title using Galada font.
 * Optimized for high performance and zero-lag rendering.
 */
@Composable
fun Stylish3DShopTitle(
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 28.sp,
    isDarkBackground: Boolean = true,
    showEnglishSub: Boolean = true,
    textAlign: TextAlign = TextAlign.Start
) {
    // Memoize the gradient brushes to avoid creating new Brush objects on recompositions
    val goldLusterBrush = remember(isDarkBackground) {
        if (isDarkBackground) {
            Brush.linearGradient(
                colors = listOf(
                    Color(0xFFFFFFFF),
                    Color(0xFFFEF08A), // Light gold highlight
                    Color(0xFFFBBF24), // Rich amber gold
                    Color(0xFFF59E0B), // Warm deep gold
                    Color(0xFFFFFFFF)  // Edge sparkle
                ),
                start = Offset(0f, 0f),
                end = Offset(450f, 80f)
            )
        } else {
            Brush.linearGradient(
                colors = listOf(
                    Color(0xFF1E3A8A), // Royal navy
                    Color(0xFF2563EB), // Blue
                    Color(0xFFDC2626), // Crimson contrast
                    Color(0xFF1E3A8A)
                ),
                start = Offset(0f, 0f),
                end = Offset(450f, 80f)
            )
        }
    }

    Column(modifier = modifier) {
        Box {
            // Layer 1: Deep 3D Cast Shadow (Extrusion depth layer)
            Text(
                text = ShopConstants.SHOP_NAME_BN,
                fontSize = fontSize,
                fontFamily = GaladaFontFamily,
                fontWeight = FontWeight.Normal,
                textAlign = textAlign,
                color = if (isDarkBackground) Color(0xFF030712).copy(alpha = 0.9f) else Color(0x331E3A8A),
                modifier = Modifier.offset(x = 2.dp, y = 3.dp)
            )

            // Layer 2: Warm 3D Chiseled Bevel Contour
            Text(
                text = ShopConstants.SHOP_NAME_BN,
                fontSize = fontSize,
                fontFamily = GaladaFontFamily,
                fontWeight = FontWeight.Normal,
                textAlign = textAlign,
                color = if (isDarkBackground) Color(0xFF92400E).copy(alpha = 0.75f) else Color(0x552563EB),
                modifier = Modifier.offset(x = 1.dp, y = 1.5.dp)
            )

            // Layer 3: Front Lustrous Surface with Shadow
            Text(
                text = ShopConstants.SHOP_NAME_BN,
                fontSize = fontSize,
                fontFamily = GaladaFontFamily,
                fontWeight = FontWeight.Normal,
                textAlign = textAlign,
                style = TextStyle(
                    brush = goldLusterBrush,
                    shadow = Shadow(
                        color = if (isDarkBackground) Color(0x80F59E0B) else Color(0x402563EB),
                        offset = Offset(0f, 1.5f),
                        blurRadius = 6f
                    )
                )
            )

            // Subtle Sparkle Icon on top corner
            if (isDarkBackground) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = Color(0xFFFEF08A),
                    modifier = Modifier
                        .size(13.dp)
                        .align(Alignment.TopEnd)
                        .offset(x = 6.dp, y = (-3).dp)
                )
            }
        }

        if (showEnglishSub) {
            Spacer(modifier = Modifier.height(2.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = if (textAlign == TextAlign.Center) Arrangement.Center else Arrangement.Start,
                modifier = if (textAlign == TextAlign.Center) Modifier.fillMaxWidth() else Modifier
            ) {
                // Gold decorative accent pill
                Box(
                    modifier = Modifier
                        .size(width = 16.dp, height = 2.5.dp)
                        .background(
                            Brush.horizontalGradient(listOf(BrandGold, BrandLightBlue)),
                            RoundedCornerShape(2.dp)
                        )
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = ShopConstants.SHOP_NAME_EN,
                    color = if (isDarkBackground) BrandGold else Color(0xFF1E293B),
                    fontSize = (fontSize.value * 0.38f).sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.6.sp,
                    fontFamily = FontFamily.Serif
                )
                Spacer(modifier = Modifier.width(6.dp))
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = if (isDarkBackground) Color(0x401E3A8A) else Color(0xFFEFF6FF),
                    border = androidx.compose.foundation.BorderStroke(
                        0.8.dp,
                        if (isDarkBackground) Color(0x6638BDF8) else Color(0xFF93C5FD)
                    )
                ) {
                    Text(
                        text = "CSC কেন্দ্র",
                        color = if (isDarkBackground) Color(0xFFBAE6FD) else Color(0xFF1E40AF),
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                    )
                }
            }
        }
    }
}
