import csv
import random
from datetime import datetime, timedelta

# Constants
NUM_ROWS = 1000000
START_DATE = datetime(2024, 4, 1)
END_DATE = datetime(2024, 4, 30)
OUTPUT_FILE = 'game_sales.csv'

# Helper function to generate a random date
def random_date(start, end):
    return start + timedelta(seconds=random.randint(0, int((end - start).total_seconds())))

# Generate CSV
with open(OUTPUT_FILE, mode='w', newline='') as file:
    writer = csv.writer(file)
    # Write header
    writer.writerow(['id', 'game_no', 'game_name', 'game_code', 'type', 'cost_price', 'tax', 'sale_price', 'date_of_sale'])

    for i in range(1, NUM_ROWS + 1):
        game_no = random.randint(1, 100)
        game_name = f"Game{random.randint(1, 999)}"
        game_code = f"G{random.randint(1, 9999):04d}"
        game_type = random.choice([1, 2])
        cost_price = round(random.uniform(0.01, 100.00), 2)
        tax = round(cost_price * 0.09, 2)
        sale_price = round(cost_price + tax, 2)
        date_of_sale = random_date(START_DATE, END_DATE).strftime('%Y-%m-%dT%H:%M:%S')

        writer.writerow([i, game_no, game_name, game_code, game_type, cost_price, tax, sale_price, date_of_sale])

print(f"CSV file '{OUTPUT_FILE}' with {NUM_ROWS} rows generated successfully.")
