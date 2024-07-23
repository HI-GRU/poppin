import '@css/Layout.css';
import { ReactNode } from 'react';
import getIncon from '@utils/get-header-icon';
import Header from "./Header";
import Menu from "./Menu";

type LayoutProps = {
    children: ReactNode;
}

const Layout01 = (props: LayoutProps) => {
  return (
    <div>
      <Header leftIcon={getIncon("notification")} rightIcon={getIncon("search")} />
      <main>{props.children}</main>
      <Menu />
    </div>
  );
};

export default Layout01;
