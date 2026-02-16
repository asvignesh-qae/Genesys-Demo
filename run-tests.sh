#!/bin/bash

# ============================================================
# GenesysDemo - Test Execution Script
# Hybrid Test Automation Framework (TestNG + Cucumber)
# ============================================================

# Colors
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
CYAN='\033[0;36m'
RED='\033[0;31m'
BOLD='\033[1m'
NC='\033[0m' # No Color

# Default values
BROWSER="chrome"
HEADLESS="false"
RETRY="2"

show_banner() {
    echo -e "${CYAN}"
    echo "============================================================"
    echo "   GenesysDemo - Hybrid Test Automation Framework"
    echo "   TestNG + Cucumber | Selenium | REST Assured | Allure"
    echo "============================================================"
    echo -e "${NC}"
}

show_menu() {
    echo -e "${BOLD}Current Settings:${NC} Browser=${GREEN}${BROWSER}${NC} | Headless=${GREEN}${HEADLESS}${NC} | Retry=${GREEN}${RETRY}${NC}"
    echo ""
    echo -e "${YELLOW}--- TestNG Suites ---${NC}"
    echo "  1) Run All Tests (Chrome Suite)"
    echo "  2) Run All Tests (Firefox Suite)"
    echo "  3) Run All Tests (Edge Suite)"
    echo "  4) Run Smoke Tests"
    echo "  5) Run Regression Tests"
    echo ""
    echo -e "${YELLOW}--- Individual TestNG Tests ---${NC}"
    echo "  6) Login Test (Data-Driven)"
    echo "  7) Purchase Test (End-to-End)"
    echo "  8) Error Message Test"
    echo "  9) Rich Text Editor Test"
    echo " 10) iFrame & Tab Handling Test"
    echo " 11) REST API Test"
    echo ""
    echo -e "${YELLOW}--- Cucumber BDD Tests ---${NC}"
    echo " 12) Run All Cucumber Scenarios"
    echo " 13) Cucumber - Purchase Automation (@testCase1)"
    echo " 14) Cucumber - Error Messages (@testCase2)"
    echo " 15) Cucumber - Rich Text Editor (@testCase3)"
    echo " 16) Cucumber - iFrame & Tab Handling (@testCase4)"
    echo " 17) Cucumber - REST API Testing (@testCase5)"
    echo " 18) Cucumber - Smoke Tests (@smoke)"
    echo ""
    echo -e "${YELLOW}--- Settings & Reports ---${NC}"
    echo " 19) Change Browser (chrome/firefox/edge)"
    echo " 20) Toggle Headless Mode"
    echo " 21) Set Cucumber Retry Count"
    echo " 22) Generate & Open Allure Report"
    echo ""
    echo "  0) Exit"
    echo ""
}

run_command() {
    echo ""
    echo -e "${CYAN}Running:${NC} $1"
    echo "------------------------------------------------------------"
    eval "$1"
    local exit_code=$?
    echo "------------------------------------------------------------"
    if [ $exit_code -eq 0 ]; then
        echo -e "${GREEN}Tests completed successfully.${NC}"
    else
        echo -e "${RED}Tests finished with failures (exit code: ${exit_code}).${NC}"
    fi
    echo ""
}

# Base maven command with common options
mvn_base() {
    echo "mvn clean test -Dbrowser=${BROWSER} -Dheadless=${HEADLESS}"
}

# Main loop
show_banner

while true; do
    show_menu
    read -p "Select an option: " choice

    case $choice in
        1)
            run_command "$(mvn_base) -DsuiteXmlFile=src/test/resources/testng-chrome.xml"
            ;;
        2)
            run_command "$(mvn_base) -DsuiteXmlFile=src/test/resources/testng-firefox.xml -Dbrowser=firefox"
            ;;
        3)
            run_command "$(mvn_base) -DsuiteXmlFile=src/test/resources/testng-edge.xml -Dbrowser=edge"
            ;;
        4)
            run_command "$(mvn_base) -DsuiteXmlFile=src/test/resources/testng-smoke.xml"
            ;;
        5)
            run_command "$(mvn_base) -DsuiteXmlFile=src/test/resources/testng-regression.xml"
            ;;
        6)
            run_command "$(mvn_base) -Dtest=com.vignesh.tests.LoginTest"
            ;;
        7)
            run_command "$(mvn_base) -Dtest=com.vignesh.tests.PurchaseTest"
            ;;
        8)
            run_command "$(mvn_base) -Dtest=com.vignesh.tests.ErrorMessageTest"
            ;;
        9)
            run_command "$(mvn_base) -Dtest=com.vignesh.tests.RichTextEditorTest"
            ;;
        10)
            run_command "$(mvn_base) -Dtest=com.vignesh.tests.FrameAndTabHandlingTest"
            ;;
        11)
            run_command "$(mvn_base) -Dtest=com.vignesh.tests.RestApiTest"
            ;;
        12)
            run_command "$(mvn_base) -Dtest=com.vignesh.runners.CucumberTestRunner -Dcucumber.execution.retry=${RETRY}"
            ;;
        13)
            run_command "$(mvn_base) -Dtest=com.vignesh.runners.CucumberTestRunner -Dcucumber.filter.tags=\"@testCase1\" -Dcucumber.execution.retry=${RETRY}"
            ;;
        14)
            run_command "$(mvn_base) -Dtest=com.vignesh.runners.CucumberTestRunner -Dcucumber.filter.tags=\"@testCase2\" -Dcucumber.execution.retry=${RETRY}"
            ;;
        15)
            run_command "$(mvn_base) -Dtest=com.vignesh.runners.CucumberTestRunner -Dcucumber.filter.tags=\"@testCase3\" -Dcucumber.execution.retry=${RETRY}"
            ;;
        16)
            run_command "$(mvn_base) -Dtest=com.vignesh.runners.CucumberTestRunner -Dcucumber.filter.tags=\"@testCase4\" -Dcucumber.execution.retry=${RETRY}"
            ;;
        17)
            run_command "$(mvn_base) -Dtest=com.vignesh.runners.CucumberTestRunner -Dcucumber.filter.tags=\"@testCase5\" -Dcucumber.execution.retry=${RETRY}"
            ;;
        18)
            run_command "$(mvn_base) -Dtest=com.vignesh.runners.CucumberTestRunner -Dcucumber.filter.tags=\"@smoke\" -Dcucumber.execution.retry=${RETRY}"
            ;;
        19)
            echo ""
            read -p "Enter browser (chrome/firefox/edge): " new_browser
            if [[ "$new_browser" == "chrome" || "$new_browser" == "firefox" || "$new_browser" == "edge" ]]; then
                BROWSER="$new_browser"
                echo -e "${GREEN}Browser set to: ${BROWSER}${NC}"
            else
                echo -e "${RED}Invalid browser. Choose chrome, firefox, or edge.${NC}"
            fi
            echo ""
            ;;
        20)
            if [ "$HEADLESS" == "false" ]; then
                HEADLESS="true"
            else
                HEADLESS="false"
            fi
            echo -e "${GREEN}Headless mode: ${HEADLESS}${NC}"
            echo ""
            ;;
        21)
            echo ""
            read -p "Enter retry count (0-5): " new_retry
            if [[ "$new_retry" =~ ^[0-5]$ ]]; then
                RETRY="$new_retry"
                echo -e "${GREEN}Cucumber retry count set to: ${RETRY}${NC}"
            else
                echo -e "${RED}Invalid value. Enter a number between 0 and 5.${NC}"
            fi
            echo ""
            ;;
        22)
            echo -e "${CYAN}Generating Allure report...${NC}"
            run_command "mvn allure:serve"
            ;;
        0)
            echo -e "${GREEN}Goodbye!${NC}"
            exit 0
            ;;
        *)
            echo -e "${RED}Invalid option. Please try again.${NC}"
            echo ""
            ;;
    esac
done
