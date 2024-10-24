# Use the Node.js 18 image for the application
FROM node:18

# Set the working directory
WORKDIR /app

# Copy package.json and package-lock.json (if available)
COPY package*.json ./

# Install the application dependencies
RUN npm install

# Copy the rest of the application code
COPY . .

# Build the application
RUN npm run build

# Expose the application port (default React app runs on 3000)
EXPOSE 3000

# Start the application
CMD ["npm", "start"]
