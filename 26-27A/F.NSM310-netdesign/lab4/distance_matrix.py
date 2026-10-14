import requests
import pandas as pd
import networkx as nx

# city: (Latitude, Longitude)
location = {
    "Ulaanbaatar": (47.9188, 106.9176),
    "Altai": (46.3722, 96.2583),
    "Arvaikheer": (46.2639, 102.7750),
    "Baruun-Urt": (46.6806, 113.2792),
    "Bayankhongor": (46.1167, 100.7167),
    "Bulgan": (48.8125, 103.5347),
    "Dalanzadgad": (43.5708, 104.4250),
    "Darkhan": (49.4867, 105.9228),
    "Zuunmod": (47.7069, 106.9750),
    "Mandalgovi": (45.7625, 106.2708),
    "Murun": (49.6342, 100.1625),
    "Sainshand": (44.8911, 110.1367),
    "Sukhbaatar": (50.2314, 106.2081),
    "Ulaangom": (50.0306, 92.0667),
    "Uliastai": (47.7417, 96.8444),
    "Khovd": (48.0056, 91.6300),
    "Tsetserleg": (47.4750, 101.4542),
    "Choibalsan": (48.0797, 114.5342),
    "Choir": (46.3611, 108.3611),
    "Ulgii": (48.9706, 89.9656),
    "Chinggis City": (47.3167, 110.6500),
    "Erdenet": (49.0278, 104.0450)
}

# prep in long, lat format
city_names = list(location.keys())
coordinates_list = []

for city in city_names:
    lat = location[city][0]
    lon = location[city][1]
    coordinates_list.append(f"{lon},{lat}")

# join all coordinates with a semicolon as required by OSRM
coords_string = ";".join(coordinates_list)

# add '?annotations=distance' to get distances instead of travel times
url = f"http://router.project-osrm.org/table/v1/driving/{coords_string}?annotations=distance"

response = requests.get(url)

if response.status_code == 200:
    data = response.json()
    
    #convert m to km
    distances_meters = data['distances']
    distances_km = []
    for row in distances_meters:
        row_km = [round(meters / 1000, 1) for meters in row]
        distances_km.append(row_km)
        
    df_distance_matrix = pd.DataFrame(distances_km, index=city_names, columns=city_names)
    #print(df_distance_matrix)
    csv_filename = "distance_matrix.csv"
    df_distance_matrix.to_csv(csv_filename, index=True)
else:
    print(f"error: {response.status_code}")
    print(response.text)
