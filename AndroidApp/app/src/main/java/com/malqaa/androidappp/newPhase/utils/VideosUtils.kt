package com.malqaa.androidappp.newPhase.utils

import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient

fun setupWebViewYouTubePlayer(webView: WebView, videoId: String?) : WebView {
    val htmlContent = """
    <!DOCTYPE html>
    <html>
      <head>
        <style>
          /* Style to make the player responsive and fill the WebView */
          #player {
            width: 100%; /* Take the full width of the WebView */
            height: 56.25vw; /* 16:9 aspect ratio (9 / 16 = 0.5625 or 56.25%) */
            max-width: 100%;
            max-height: 100%;
          }
        </style>
      </head>
      <body>
        <div id="player"></div>
        <script>
          var tag = document.createElement('script');
          tag.src = "https://www.youtube.com/iframe_api";
          var firstScriptTag = document.getElementsByTagName('script')[0];
          firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);

          var player;
          function onYouTubeIframeAPIReady() {
            player = new YT.Player('player', {
              videoId: '$videoId',
              playerVars: {
                'playsinline': 1,
                'autoplay': 1
              },
              events: {
                'onReady': onPlayerReady
              }
            });
          }

          function onPlayerReady(event) {
            event.target.playVideo(); // Start video playback when ready
          }
        </script>
      </body>
    </html>
""".trimIndent()

    webView.apply {
        webViewClient = WebViewClient()
        webChromeClient = WebChromeClient()
        settings.apply {
            javaScriptEnabled = true
            domStorageEnabled = true
            mediaPlaybackRequiresUserGesture = false
            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            // Enable built-in zoom controls
            builtInZoomControls = true
            displayZoomControls = false // Set to true if you want to show the zoom controls
        }

        // Load the HTML content with a base URL
        loadDataWithBaseURL(
            "https://www.youtube.com",
            htmlContent,
            "text/html",
            "UTF-8",
            null
        )
    }

    return webView

}