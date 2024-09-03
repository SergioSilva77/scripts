FROM ibmfunctions/action-python-v3.7

RUN apt-get update && \
    apt-get install -y wget unzip bc vim python3-pip libleptonica-dev git
# Packages to complie Tesseract
RUN apt-get install -y --reinstall make && \
    apt-get install -y g++ autoconf automake libtool pkg-config libpng-dev libjpeg62-turbo-dev libtiff5-dev libicu-dev \
        libpango1.0-dev autoconf-archive
# Set working directory
WORKDIR /app
COPY . /app
# Copy requirements into the container at /app
COPY requirements.txt ./
# Getting tesstrain: beware the source might change or not being available
# Complie Tesseract with training options (also feel free to update Tesseract versions and such!)
# Getting data: beware the source might change or not being available
RUN mkdir src && cd /app/src && \
    wget https://github.com/tesseract-ocr/tesseract/archive/4.1.0.zip && \
	unzip 4.1.0.zip && \
    cd /app/src/tesseract-4.1.0 && ./autogen.sh && ./configure && make && make install && ldconfig && \
    make training && make training-install && \
    cd /usr/local/share/tessdata && wget https://github.com/tesseract-ocr/tessdata/raw/main/por.traineddata
# Setting the data prefix
ENV TESSDATA_PREFIX=/usr/local/share/tessdata

RUN pip install \
   --upgrade pip
RUN pip install pytesseract opencv-python==4.1.2.30
