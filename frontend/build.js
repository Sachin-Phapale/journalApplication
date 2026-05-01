const fs = require('fs');
const path = require('path');

// Create the static resources directory in the target folder
const staticDir = path.join(__dirname, '..', 'target', 'classes', 'static');
const buildDir = path.join(__dirname, 'build');

if (!fs.existsSync(staticDir)) {
    fs.mkdirSync(staticDir, { recursive: true });
}

// Copy build files to static directory
if (fs.existsSync(buildDir)) {
    const copyRecursive = (src, dest) => {
        const exists = fs.existsSync(src);
        const stats = exists && fs.statSync(src);
        const isDirectory = exists && stats.isDirectory();
        
        if (isDirectory) {
            if (!fs.existsSync(dest)) {
                fs.mkdirSync(dest, { recursive: true });
            }
            fs.readdirSync(src).forEach(childItemName => {
                copyRecursive(path.join(src, childItemName), path.join(dest, childItemName));
            });
        } else {
            fs.copyFileSync(src, dest);
        }
    };
    
    copyRecursive(buildDir, staticDir);
    console.log('Frontend build files copied to Spring Boot static directory');
} else {
    console.log('Build directory not found. Run "npm run build-only" first.');
}
