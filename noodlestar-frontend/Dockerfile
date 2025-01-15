# Use an official Node.js runtime as the base image
FROM node:21 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy package.json and package-lock.json to the working directory
COPY package.json package-lock.json ./

# Install project dependencies
RUN npm install

# Copy the rest of the application code to the working directory
COPY . .

# Expose port 3000 (the default port for React development server)
EXPOSE 3000

# Command to start the React development server
CMD ["npm", "start"]
