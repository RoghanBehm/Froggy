# Compiler and runtime
JAVAC = javac
JAVA = java

# Directories
SRC_DIR = froggy/src/froggy
BUILD_DIR = build

# Find all Java files
SOURCES = $(wildcard $(SRC_DIR)/*.java)

# Main class
MAIN_CLASS = froggy.src.froggy.Froggy

# Default target
.PHONY: all
all: compile

# Compile all Java files to build directory
.PHONY: compile
compile:
	mkdir -p $(BUILD_DIR)
	$(JAVAC) -d $(BUILD_DIR) $(SOURCES)

# Run with a file argument
.PHONY: run
run: compile
	$(JAVA) -cp $(BUILD_DIR) $(MAIN_CLASS) $(ARGS)

# Run REPL mode
.PHONY: repl
repl: compile
	$(JAVA) -cp $(BUILD_DIR) $(MAIN_CLASS)

# Clean up build directory
.PHONY: clean
clean:
	rm -rf $(BUILD_DIR)

# Help
.PHONY: help
help:
	@echo "Usage:"
	@echo "  make compile  - Compile all Java files"
	@echo "  make run ARGS=file.frog - Run with a file"
	@echo "  make repl     - Run interactive REPL"
	@echo "  make clean    - Remove build directory"