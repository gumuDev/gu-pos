import React from 'react';
import './landing.css';
import Link from 'next/link';

export default function LandingPage() {
  return (
    <div className="landing-container">
      {/* Navbar */}
      <nav className="navbar">
        <div className="nav-logo">
          <div style={{ width: 32, height: 32, background: 'var(--lp-primary)', borderRadius: 8, display: 'flex', alignItems: 'center', justifyContent: 'center', color: 'white', fontWeight: 'bold' }}>G</div>
          Gu-Pos
        </div>
        <div className="nav-links">
          <a href="#features" className="nav-link">Características</a>
          <a href="#types" className="nav-link">Tipos de Negocio</a>
          <a href="#app" className="nav-link">App</a>
          <a href="#pricing" className="nav-link">Planes</a>
        </div>
        <div className="nav-actions">
          {/* Botones de Login y Comenzar Gratis removidos */}
          <span style={{ color: 'var(--lp-text-muted)', fontSize: '0.9rem' }}>Tu negocio en tus manos</span>
        </div>
      </nav>

      {/* Hero Section */}
      <header className="hero">
        <div className="hero-content">
          <span className="hero-tag">Punto de Venta Local y en la Nube</span>
          <h1 className="hero-title">Gestiona tus ventas con total libertad</h1>
          <p className="hero-description">
            Una solución potente diseñada para trabajar de forma local y sincronizar con la nube. Ideal para negocios que buscan agilidad y control total.
          </p>
          <div style={{ display: 'flex', gap: 16 }}>
            <a href="https://drive.google.com/file/d/1aj6ednJPHiKc7t-_Pi8i4Jlp39ar_6ug/view?usp=drive_link" target="_blank" rel="noopener noreferrer" className="btn btn-primary" style={{ padding: '14px 28px', fontSize: '1.1rem' }}>Descargar App</a>
            <a href="#features" className="btn btn-outline" style={{ padding: '14px 28px', fontSize: '1.1rem' }}>Saber más</a>
          </div>
        </div>
        <div className="hero-image">
          <div className="mockup-container">
            <img 
              src="/images/landing/main-hero.jpeg" 
              alt="Gu-Pos Pantalla de Ventas" 
              className="main-mockup"
            />
          </div>
        </div>
      </header>

      {/* Features Section */}
      <section id="features" className="section">
        <div className="section-header">
          <h2 className="section-title">Funcionalidades Principales</h2>
          <p className="section-subtitle">Potencia tu operativa diaria con herramientas desarrolladas específicamente para el mercado actual.</p>
        </div>
        
        <div className="features-grid">
          <div className="feature-card">
            <div className="feature-icon">🛒</div>
            <h3 className="feature-title">Ventas Offline</h3>
            <p className="feature-text">Sigue vendiendo incluso sin conexión a internet. Tus datos se sincronizarán automáticamente cuando vuelvas a estar en línea.</p>
          </div>
          
          <div className="feature-card">
            <div className="feature-icon">📦</div>
            <h3 className="feature-title">Gestión de Inventario</h3>
            <p className="feature-text">Control de inventario local incluido. Con una suscripción activa, desbloquea la sincronización en tiempo real entre múltiples sucursales.</p>
          </div>
          
          <div className="feature-card">
            <div className="feature-icon">📊</div>
            <h3 className="feature-title">Reportes Avanzados</h3>
            <p className="feature-text">Visualiza tus métricas de ventas, productos más vendidos y rendimiento por empleado desde cualquier lugar.</p>
          </div>
        </div>
      </section>

      {/* Tipos de Negocio Section */}
      <section id="types" className="section" style={{ background: 'var(--lp-bg-alt)' }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: '60px', flexWrap: 'wrap' }}>
          <div style={{ flex: '1', minWidth: '300px' }}>
            <h2 className="section-title">Adaptado a tu Tipo de Negocio</h2>
            <p className="hero-description">Gu-Pos es versátil y se adapta a las necesidades específicas de diversos sectores:</p>
            <ul style={{ listStyle: 'none', padding: 0, marginTop: '24px' }}>
              <li style={{ marginBottom: '16px', display: 'flex', alignItems: 'center', gap: '12px', fontSize: '1.1rem', fontWeight: 600 }}>
                <span style={{ color: 'var(--lp-primary)' }}>✓</span> Restaurantes y Cafeterías
              </li>
              <li style={{ marginBottom: '16px', display: 'flex', alignItems: 'center', gap: '12px', fontSize: '1.1rem', fontWeight: 600 }}>
                <span style={{ color: 'var(--lp-primary)' }}>✓</span> Pizzerías y Fast Food
              </li>
              <li style={{ marginBottom: '16px', display: 'flex', alignItems: 'center', gap: '12px', fontSize: '1.1rem', fontWeight: 600 }}>
                <span style={{ color: 'var(--lp-primary)' }}>✓</span> Tiendas de Ropa y Retail
              </li>
              <li style={{ marginBottom: '16px', display: 'flex', alignItems: 'center', gap: '12px', fontSize: '1.1rem', fontWeight: 600 }}>
                <span style={{ color: 'var(--lp-primary)' }}>✓</span> Minimarkets y Farmacias
              </li>
            </ul>
          </div>
          <div style={{ flex: '1', minWidth: '300px', display: 'flex', justifyContent: 'center' }}>
            <img 
              src="/images/landing/business-types.jpeg" 
              alt="Tipos de Negocio" 
              style={{ width: '100%', maxWidth: '450px', borderRadius: '24px', boxShadow: 'var(--lp-shadow)' }}
            />
          </div>
        </div>
      </section>

      {/* App Download Section */}
      <section id="app" className="section">
        <div style={{ display: 'flex', alignItems: 'center', gap: '60px', flexWrap: 'wrap-reverse' }}>
          <div style={{ flex: '1', minWidth: '300px', display: 'flex', justifyContent: 'center' }}>
            <img 
              src="/images/landing/app-download.jpeg" 
              alt="Descarga nuestra App" 
              style={{ width: '100%', maxWidth: '400px', borderRadius: '32px', boxShadow: 'var(--lp-shadow)' }}
            />
          </div>
          <div style={{ flex: '1', minWidth: '300px' }}>
            <h2 className="section-title">Lleva tu POS a todas partes</h2>
            <p className="hero-description">
              Descarga nuestra aplicación oficial y empieza a gestionar tu negocio desde tu dispositivo móvil o tablet de manera eficiente.
            </p>
            <div style={{ marginTop: '32px' }}>
              <a href="https://drive.google.com/uc?export=download&id=12gpsBd-1m0t57qDB0URNFo4feWtAaONn" target="_blank" rel="noopener noreferrer" className="btn btn-primary" style={{ padding: '16px 40px', fontSize: '1.2rem', display: 'inline-flex', alignItems: 'center', gap: '12px' }}>
                 Descargar APK
              </a>
              <p style={{ marginTop: '16px', fontSize: '0.875rem', color: 'var(--lp-text-muted)' }}>Versión 2.0.4 - Compatible con Android 8.0+</p>
            </div>
          </div>
        </div>
      </section>

      {/* Pricing Section */}
      <section id="pricing" className="section" style={{ background: 'var(--lp-bg-alt)' }}>
        <div className="section-header">
          <h2 className="section-title">Planes</h2>
          <p className="section-subtitle">Elige el plan que mejor se adapte al volumen y necesidades de tu negocio.</p>
        </div>
        
        <div className="pricing-grid">
          <div className="pricing-card">
            <h3 className="pricing-plan">Free</h3>
            <div className="pricing-price">0 Bs<span>/mes</span></div>
            <p className="pricing-desc">Perfecto para empezar a digitalizar tu negocio localmente.</p>
            <ul className="pricing-features">
              <li>✓ Historial del día actual</li>
              <li>✓ Reportes de ventas de hoy</li>
              <li>✓ Importación de datos en csv o excel</li>
              <li>✓ Uso local (sin permanencia)</li>
              <li className="disabled">✕ Sincronización en la nube</li>
              <li className="disabled">✕ Exportación de datos</li>
            </ul>
          </div>
          
          <div className="pricing-card featured">
            <div className="featured-badge">Más Popular</div>
            <h3 className="pricing-plan">Básico</h3>
            <div className="pricing-price">20 Bs<span>/mes</span></div>
            <p className="pricing-desc">Ideal para negocios que necesitan respaldo y reportes avanzados.</p>
            <ul className="pricing-features">
              <li>✓ <strong>Todo lo del plan Free</strong></li>
              <li>✓ Sincronización automática Cloud</li>
              <li>✓ Historial ilimitado y búsqueda</li>
              <li>✓ Reportes avanzados (Semana/Mes)</li>
              <li>✓ Exportación a Excel (.xlsx)</li>
              <li>✓ Alertas de Stock por Telegram</li>
            </ul>
          </div>
          
          <div className="pricing-card">
            <h3 className="pricing-plan">Pro</h3>
            <div className="pricing-price">50 Bs<span>/mes</span></div>
            <p className="pricing-desc">Para dueños que quieren control total y automatización.</p>
            <ul className="pricing-features">
              <li>✓ <strong>Todo lo del plan Básico</strong></li>
              <li>✓ Resumen diario por Telegram</li>
              <li>✓ Ticket promedio y Top productos</li>
              <li>✓ Soporte prioritario</li>
              <li>✓ Próximamente: Multi-sucursal</li>
            </ul>
          </div>
        </div>
      </section>

      {/* Footer */}
      <footer className="footer">
        <div className="footer-grid">
          <div>
            <div className="nav-logo" style={{ color: 'white', marginBottom: 24 }}>Gu-Pos</div>
            <p style={{ color: '#94a3b8', lineHeight: 1.6 }}>La herramienta definitiva para la gestión local y remota de tu punto de venta.</p>
          </div>
          <div>
            <h4 className="footer-title">Producto</h4>
            <ul className="footer-links">
              <li className="footer-link"><a href="#features">Características</a></li>
              <li className="footer-link"><a href="#types">Tipos de Negocio</a></li>
              <li className="footer-link"><a href="#app">Descargar App</a></li>
            </ul>
          </div>
          <div>
            <h4 className="footer-title">Legal</h4>
            <ul className="footer-links">
              <li className="footer-link"><a href="#">Términos de Servicio</a></li>
              <li className="footer-link"><a href="#">Privacidad</a></li>
            </ul>
          </div>
          <div>
            <h4 className="footer-title">Contacto</h4>
            <ul className="footer-links">
              <li className="footer-link"><a href="#">Soporte Técnico</a></li>
              <li className="footer-link"><a href="#">Ventas</a></li>
            </ul>
          </div>
        </div>
        <div className="footer-bottom">
          <p>© 2026 Gu-Pos. Hecho por <a href="https://gumudev.github.io/opengumu/" target="_blank" rel="noopener noreferrer" style={{ color: 'var(--lp-primary)', textDecoration: 'none', fontWeight: 'bold' }}>openGumu</a>. Todos los derechos reservados.</p>
        </div>
      </footer>
    </div>
  );
}
