import requests
import csv

# Define the API endpoint
API_URL = "http://192.168.1.6:8080/api/location/get-lat-lng"

# Function to get latitude and longitude from API
def get_lat_lng(url):
    full_url = f"{API_URL}?url={url}"
    print(f"Requesting: {full_url}")  # Debugging line
    response = requests.get(full_url)
    
    if response.status_code == 200:
        data = response.json()
        return data.get("lat"), data.get("lng")
    else:
        print(f"Error fetching data for {url}, Response: {response.text}")
        return None, None


# Read the CSV file and update with lat, lng
input_file = "live_locations.csv"  # Change this to your actual CSV file
output_file = "updated_places.csv"

updated_rows = []

with open(input_file, "r", encoding="utf-8") as file:
    reader = csv.reader(file, delimiter="~")
    for row in reader:
        if len(row) < 2:
            continue  # Skip invalid rows
        name, url = row[0], row[1]
        lat, lng = get_lat_lng(url)
        updated_rows.append([name, url, lat, lng])

# Write updated data back to a new CSV file
with open(output_file, "w", encoding="utf-8", newline="") as file:
    writer = csv.writer(file, delimiter="~")
    writer.writerows(updated_rows)

print("File updated successfully!")