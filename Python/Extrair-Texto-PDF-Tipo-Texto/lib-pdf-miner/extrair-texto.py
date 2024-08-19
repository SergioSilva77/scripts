from pdfminer.high_level import extract_pages
from pdfminer.layout import LTTextContainer, LTChar,LTLine,LAParams
import os

path=r'/content/105221-2.199,20.pdf'
Extract_Data = []
for page_layout in extract_pages(path):
            for element in page_layout:
                if isinstance(element, LTTextContainer):
                    for text_line in element:
                        for character in text_line:
                            if isinstance(character, LTChar):
                                Font_size=character.size
                    Extract_Data.append([Font_size,(element.get_text())])