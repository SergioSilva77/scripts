# Capturar tela

```jsx
 static Bitmap CaptureScreen()
 {
     Rectangle bounds = Screen.PrimaryScreen.Bounds;
     Bitmap bitmap = new Bitmap(bounds.Width, bounds.Height, PixelFormat.Format32bppArgb);
     using (Graphics g = Graphics.FromImage(bitmap))
     {
         g.CopyFromScreen(bounds.X, bounds.Y, 0, 0, bounds.Size, CopyPixelOperation.SourceCopy);
     }
     return bitmap;
 }
```