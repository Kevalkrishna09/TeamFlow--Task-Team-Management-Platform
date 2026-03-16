import React from 'react'
import { useAuth } from '../context/AuthContext';
import { useNavigate } from 'react-router-dom';
import userLogo from '../assets/userlogo.png';
export const Header = () => {
  const [dropdownOpen, setDropdownOpen] = React.useState(false);

  const navigate = useNavigate();
  const { logout } = useAuth();
  const handleLogOut = () => {
    logout();
    navigate("/login");
  }

  const toggleDropdown = () => {
    console.log("dropdownOpen ", dropdownOpen);
    setDropdownOpen(!dropdownOpen);
  };

  return (
    <header className="flex shadow-md py-4 px-4 sm:px-10 bg-white min-h-[70px] tracking-wide relative z-50">
      <div className="flex flex-wrap items-center justify-between gap-5 w-full">
        <a href="javascript:void(0)" className="max-sm:hidden"><img src="https://readymadeui.com/readymadeui.svg" alt="logo" className="w-36" /></a>
        <a href="javascript:void(0)" className="hidden max-sm:block"><img src="https://readymadeui.com/readymadeui-short.svg" alt="logo" className="w-9" /></a>

        <div id="collapseMenu"
          className="max-lg:hidden lg:!block max-lg:before:fixed max-lg:before:bg-black max-lg:before:opacity-50 max-lg:before:inset-0 max-lg:before:z-50">
          <button id="toggleClose" className="lg:hidden fixed top-2 right-4 z-[100] rounded-full bg-white w-9 h-9 flex items-center justify-center border border-gray-200 cursor-pointer">
            <svg xmlns="http://www.w3.org/2000/svg" className="w-3.5 h-3.5 fill-black" viewBox="0 0 320.591 320.591">
              <path
                d="M30.391 318.583a30.37 30.37 0 0 1-21.56-7.288c-11.774-11.844-11.774-30.973 0-42.817L266.643 10.665c12.246-11.459 31.462-10.822 42.921 1.424 10.362 11.074 10.966 28.095 1.414 39.875L51.647 311.295a30.366 30.366 0 0 1-21.256 7.288z"
                data-original="#000000"></path>
              <path
                d="M287.9 318.583a30.37 30.37 0 0 1-21.257-8.806L8.83 51.963C-2.078 39.225-.595 20.055 12.143 9.146c11.369-9.736 28.136-9.736 39.504 0l259.331 257.813c12.243 11.462 12.876 30.679 1.414 42.922-.456.487-.927.958-1.414 1.414a30.368 30.368 0 0 1-23.078 7.288z"
                data-original="#000000"></path>
            </svg>
          </button>

          <ul
            className="lg:flex gap-x-4 max-lg:space-y-3 max-lg:fixed max-lg:bg-white max-lg:w-1/2 max-lg:min-w-[300px] max-lg:top-0 max-lg:left-0 max-lg:p-6 max-lg:h-full max-lg:shadow-md max-lg:overflow-auto z-50">

            <li className="max-lg:border-b max-lg:border-gray-300 max-lg:py-3 px-3">
              <button type="button" className="text-white bg-black box-border border border-transparent hover:bg-dark-strong focus:ring-4 focus:ring-neutral-tertiary shadow-xs font-medium leading-5 rounded-full text-sm px-4 py-2.5 focus:outline-none"
                onClick={() => handleLogOut()}
              >Log out </button>
            </li>

            <li className=" relative max-lg:border-b max-lg:border-gray-300 max-lg:py-3 px-3">
              <button onClick={toggleDropdown} className="flex items-center text-sm text-gray-500 hover:text-gray-700 focus:outline-none"><img src={userLogo} alt="user" className="w-10 inline-block mr-1" /></button>

              {dropdownOpen && (
                <div id="dropdown"  className="absolute right-0 top-full mt-1 z-10 bg-white border border-gray-200 rounded-lg shadow-lg w-32">

                  <ul className="p-2 text-sm text-gray-700 font-medium">
                    <li>
                      <a href="#" className="inline-flex items-center w-full p-2 hover:bg-gray-100 rounded-md">Profile</a>
                    </li>
                    <li>
                      <a href="#" className="inline-flex items-center w-full p-2 hover:bg-gray-100 rounded-md">Settings</a>
                    </li>
                    <li>
                      <a href="#" className="inline-flex items-center w-full p-2 hover:bg-gray-100 rounded-md">Logout</a>
                    </li>
                  </ul>
                </div>
              )}
            </li>
          </ul>
        </div>
      </div>
    </header>
  )
}
