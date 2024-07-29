using OpenCvSharp.Extensions;
using OpenCvSharp;
using System;
using System.Collections.Generic;
using System.Drawing;
using System.Drawing.Imaging;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using WindowsInput;
using System.Windows.Forms;

namespace LibClicarImagem.Src.ClicarPorImagem
{
    public class ClicarPorImagem
    {
        public static void Clicar(string imagePath, ImageSide imageSide = ImageSide.CENTER, double threshold = 0.8)
        {
            // Capturar a tela
            Bitmap screenshot = CaptureScreen();

            // Localizar a imagem na tela
            System.Drawing.Point? location = FindImageOnScreen(screenshot, imagePath, threshold);

            if (location != null)
            {
                location = AdjustLocation(location.Value, imageSide);

                // Simular o clique do mouse na localização encontrada
                ClickAt(location.Value);
            }
            else
            {
                throw new Exception("Imagem não encontrada na tela.");
            }
        }

        private static Bitmap CaptureScreen()
        {
            Rectangle bounds = Screen.PrimaryScreen.Bounds;
            Bitmap bitmap = new Bitmap(bounds.Width, bounds.Height, PixelFormat.Format32bppArgb);
            using (Graphics g = Graphics.FromImage(bitmap))
            {
                g.CopyFromScreen(bounds.X, bounds.Y, 0, 0, bounds.Size, CopyPixelOperation.SourceCopy);
            }
            return bitmap;
        }

        public static System.Drawing.Point? FindImageOnScreen(Bitmap screenshot, string imagePath, double threshold)
        {
            Mat screenMat = BitmapConverter.ToMat(screenshot);
            Mat template = Cv2.ImRead(imagePath, ImreadModes.Color);

            // Convertendo para o mesmo tipo se necessário
            if (screenMat.Type() != template.Type())
            {
                Cv2.CvtColor(screenMat, screenMat, ColorConversionCodes.BGR2GRAY);
                Cv2.CvtColor(template, template, ColorConversionCodes.BGR2GRAY);
            }

            // Realizar correspondência de template
            Mat result = new Mat();
            Cv2.MatchTemplate(screenMat, template, result, TemplateMatchModes.CCoeffNormed);

            // Localizar o ponto com a melhor correspondência
            Cv2.MinMaxLoc(result, out _, out double maxVal, out _, out OpenCvSharp.Point maxLoc);

            // Definir um limiar para correspondência (ajuste conforme necessário)
            if (maxVal >= threshold)
            {
                return new System.Drawing.Point(maxLoc.X + template.Width / 2, maxLoc.Y + template.Height / 2);
            }
            return null;
        }

        public static void ClickAt(System.Drawing.Point location)
        {
            var sim = new InputSimulator();
            int screenWidth = 65535;
            int screenHeight = 65535;

            // Converter as coordenadas do ponto para coordenadas de tela do mouse
            double x = (location.X / (double)Screen.PrimaryScreen.Bounds.Width) * screenWidth;
            double y = (location.Y / (double)Screen.PrimaryScreen.Bounds.Height) * screenHeight;

            // Mover o mouse para a localização e clicar
            sim.Mouse.MoveMouseToPositionOnVirtualDesktop((int)x, (int)y);
            sim.Mouse.LeftButtonClick();
        }

        private static System.Drawing.Point AdjustLocation(System.Drawing.Point location, ImageSide imageSide)
        {
            switch (imageSide)
            {
                case ImageSide.TOP_LEFT:
                    return new System.Drawing.Point(location.X - 50, location.Y - 50); // Adjust the offsets as needed
                case ImageSide.TOP_RIGHT:
                    return new System.Drawing.Point(location.X + 50, location.Y - 50); // Adjust the offsets as needed
                case ImageSide.BOTTOM_LEFT:
                    return new System.Drawing.Point(location.X - 50, location.Y + 50); // Adjust the offsets as needed
                case ImageSide.BOTTOM_RIGHT:
                    return new System.Drawing.Point(location.X + 50, location.Y + 50); // Adjust the offsets as needed
                default: // CENTER
                    return location;
            }
        }
    }
}
