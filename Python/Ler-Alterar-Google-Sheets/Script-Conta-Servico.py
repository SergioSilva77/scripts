from google.oauth2 import service_account
from googleapiclient.discovery import build
from google.auth.transport.requests import Request
from google.oauth2.credentials import Credentials
from google_auth_oauthlib.flow import InstalledAppFlow
from googleapiclient.discovery import build
from googleapiclient.errors import HttpError

SERVICE_ACCOUNT_FILE = 'credentials.json'
SAMPLE_SPREADSHEET_ID = "aaaaaaaaaa"
SAMPLE_RANGE_NAME = "RPAs!A1:B1"

credentials = service_account.Credentials.from_service_account_file(
    filename=SERVICE_ACCOUNT_FILE
)

service_sheets = build('sheets', 'v4', credentials=credentials)

sheet = service_sheets.spreadsheets()
valores = [
    ['Timbro', 'Processado2', '09/08/2024 11:13']
]
result = (
    sheet.values()
    .update(spreadsheetId=SAMPLE_SPREADSHEET_ID, 
            range='A2', 
            valueInputOption='USER_ENTERED', 
            body={'values': valores})
    .execute()
)